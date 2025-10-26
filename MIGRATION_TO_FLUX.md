# Migration Guide: Java Stream to Reactor Flux

本指南帮助您将代码从 Java Stream 迁移到 Reactor Flux。

## 概述

Framework 已从 Java Stream API 迁移到 Project Reactor 的 Flux，以提供更好的异步、非阻塞流处理能力。

## 核心变化

### Before (使用 Stream)
```java
Stream<ChatModelResponse> stream = chatModel.stream(request);
```

### After (使用 Flux)
```java
Flux<ChatModelResponse> flux = chatModel.stream(request);
```

## 迁移步骤

### 1. 简单的 forEach 操作

**Before**:
```java
chatModel.stream(request).forEach(chunk -> {
    System.out.print(extractText(chunk));
});
```

**After**:
```java
chatModel.stream(request).subscribe(chunk -> {
    System.out.print(extractText(chunk));
});
```

**注意**: `subscribe()` 是非阻塞的，如果需要阻塞行为，见下文。

### 2. 收集所有结果

**Before**:
```java
List<ChatModelResponse> results = chatModel.stream(request)
    .collect(Collectors.toList());
```

**After**:
```java
List<ChatModelResponse> results = chatModel.stream(request)
    .collectList()
    .block();  // 阻塞等待
```

**或者使用响应式方式**:
```java
chatModel.stream(request)
    .collectList()
    .subscribe(results -> {
        // 处理完整列表
        processResults(results);
    });
```

### 3. 过滤和映射

**Before**:
```java
chatModel.stream(request)
    .filter(chunk -> !chunk.isLast())
    .map(this::extractText)
    .forEach(System.out::println);
```

**After**:
```java
chatModel.stream(request)
    .filter(chunk -> !chunk.isLast())
    .map(this::extractText)
    .subscribe(System.out::println);
```

### 4. 处理错误

**Before**:
```java
try {
    chatModel.stream(request).forEach(this::process);
} catch (Exception e) {
    handleError(e);
}
```

**After**:
```java
chatModel.stream(request).subscribe(
    this::process,           // onNext
    this::handleError,       // onError
    () -> onComplete()       // onComplete
);
```

### 5. 限制数量

**Before**:
```java
chatModel.stream(request)
    .limit(10)
    .forEach(this::process);
```

**After**:
```java
chatModel.stream(request)
    .take(10)
    .subscribe(this::process);
```

### 6. 跳过元素

**Before**:
```java
chatModel.stream(request)
    .skip(5)
    .forEach(this::process);
```

**After**:
```java
chatModel.stream(request)
    .skip(5)
    .subscribe(this::process);
```

### 7. 查找第一个

**Before**:
```java
Optional<ChatModelResponse> first = chatModel.stream(request)
    .findFirst();
```

**After**:
```java
// 非阻塞
Mono<ChatModelResponse> first = chatModel.stream(request)
    .next();  // 返回 Mono

// 阻塞
ChatModelResponse first = chatModel.stream(request)
    .next()
    .block();
```

### 8. 任意匹配

**Before**:
```java
boolean hasError = chatModel.stream(request)
    .anyMatch(chunk -> chunk.getResult().getType() == ResultType.ERROR);
```

**After**:
```java
boolean hasError = chatModel.stream(request)
    .any(chunk -> chunk.getResult().getType() == ResultType.ERROR)
    .block();  // 返回 Mono<Boolean>
```

### 9. 计数

**Before**:
```java
long count = chatModel.stream(request).count();
```

**After**:
```java
long count = chatModel.stream(request)
    .count()
    .block();
```

### 10. 使用 StreamHandler

如果您使用了 `StreamHandler` 接口，可以使用新的 `FluxStreamHandler` 工具类：

**Before**:
```java
StreamHandler<ChatModelResponse> handler = createHandler();
chatModel.stream(request).forEach(handler::onChunk);
handler.onComplete();
```

**After**:
```java
StreamHandler<ChatModelResponse> handler = createHandler();
Flux<ChatModelResponse> flux = chatModel.stream(request);
FluxStreamHandler.subscribe(flux, handler);
```

## Flux 的额外功能

迁移到 Flux 后，您可以使用许多强大的新功能：

### 1. 超时控制

```java
chatModel.stream(request)
    .timeout(Duration.ofSeconds(30))
    .subscribe(this::process);
```

### 2. 重试机制

```java
chatModel.stream(request)
    .retry(3)  // 失败时重试 3 次
    .subscribe(this::process);
```

### 3. 错误恢复

```java
chatModel.stream(request)
    .onErrorResume(error -> {
        log.error("Primary failed, using fallback", error);
        return fallbackModel.stream(request);
    })
    .subscribe(this::process);
```

### 4. 背压处理

```java
chatModel.stream(request)
    .onBackpressureBuffer(100)  // 缓冲 100 个元素
    .subscribe(this::process);
```

### 5. 延迟处理

```java
chatModel.stream(request)
    .delayElements(Duration.ofMillis(100))  // 每个元素延迟 100ms
    .subscribe(this::process);
```

### 6. 合并多个流

```java
Flux<ChatModelResponse> merged = Flux.merge(
    model1.stream(request1),
    model2.stream(request2)
);
merged.subscribe(this::process);
```

### 7. 组合流

```java
Flux.zip(
    chatModel.stream(request),
    embeddingModel.stream(embeddingRequest)
).subscribe(tuple -> {
    ChatModelResponse chat = tuple.getT1();
    EmbeddingModelResponse embedding = tuple.getT2();
    processBundle(chat, embedding);
});
```

## 常见模式对照表

| 操作 | Java Stream | Reactor Flux |
|-----|-------------|--------------|
| 遍历 | `.forEach(f)` | `.subscribe(f)` |
| 映射 | `.map(f)` | `.map(f)` |
| 过滤 | `.filter(p)` | `.filter(p)` |
| 限制 | `.limit(n)` | `.take(n)` |
| 跳过 | `.skip(n)` | `.skip(n)` |
| 去重 | `.distinct()` | `.distinct()` |
| 排序 | `.sorted()` | `.sort()` |
| 扁平化 | `.flatMap(f)` | `.flatMap(f)` |
| 收集 | `.collect(...)` | `.collectList()/.collectMap()` |
| 计数 | `.count()` | `.count().block()` |
| 第一个 | `.findFirst()` | `.next().block()` |
| 任意 | `.findAny()` | `.next().block()` |
| 匹配 | `.anyMatch(p)` | `.any(p).block()` |
| 全部匹配 | `.allMatch(p)` | `.all(p).block()` |
| 累积 | `.reduce(...)` | `.scan(...)` 或 `.reduce(...)` |

## 阻塞 vs 非阻塞

**重要**: Flux 默认是非阻塞的。

### 阻塞方式（不推荐用于生产）

```java
// 等待流完成
flux.blockLast();

// 获取第一个元素
ChatModelResponse first = flux.blockFirst();

// 收集所有结果
List<ChatModelResponse> results = flux.collectList().block();
```

### 推荐的非阻塞方式

```java
flux.subscribe(
    item -> process(item),
    error -> handleError(error),
    () -> onComplete()
);
```

## Spring WebFlux 集成

如果您使用 Spring WebFlux，可以直接返回 Flux：

```java
@RestController
public class ChatController {
    
    @PostMapping(value = "/chat/stream", 
                 produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request) {
        return chatModel.stream(toModelRequest(request))
            .map(this::extractText)
            .filter(text -> !text.isEmpty())
            .map(text -> ServerSentEvent.<String>builder()
                .data(text)
                .build());
    }
}
```

## 测试

使用 Reactor Test 进行测试：

```java
import reactor.test.StepVerifier;

@Test
void testStreaming() {
    Flux<ChatModelResponse> flux = chatModel.stream(request);
    
    StepVerifier.create(flux)
        .expectNextCount(5)
        .expectComplete()
        .verify();
}

@Test
void testError() {
    Flux<ChatModelResponse> flux = chatModel.stream(invalidRequest);
    
    StepVerifier.create(flux)
        .expectError(InvalidRequestException.class)
        .verify();
}
```

## 性能建议

1. **避免阻塞**: 尽量使用 `subscribe()` 而不是 `block()`
2. **选择合适的调度器**: 
   - I/O 操作: `Schedulers.boundedElastic()`
   - CPU 密集: `Schedulers.parallel()`
3. **控制缓冲**: 使用 `onBackpressureBuffer(n)` 限制内存
4. **及时释放资源**: 使用 `doFinally()` 清理资源

## 故障排查

### 问题: 代码不执行

```java
// ❌ 错误：subscribe() 被立即返回
Flux<ChatModelResponse> flux = chatModel.stream(request);
flux.map(this::process);  // 没有订阅，不会执行
```

```java
// ✅ 正确：添加 subscribe
Flux<ChatModelResponse> flux = chatModel.stream(request);
flux.map(this::process).subscribe();
```

### 问题: 需要等待完成

```java
// 使用 CountDownLatch
CountDownLatch latch = new CountDownLatch(1);
flux.doFinally(signal -> latch.countDown())
    .subscribe(this::process);
latch.await();

// 或直接 block（不推荐）
flux.blockLast();
```

### 问题: 异常未被捕获

```java
// ✅ 正确：在 subscribe 中处理错误
flux.subscribe(
    this::process,
    error -> log.error("Error occurred", error)
);
```

## 更多资源

- [REACTIVE_STREAMING.md](REACTIVE_STREAMING.md) - 完整的 Flux 使用指南
- [Project Reactor 文档](https://projectreactor.io/docs)
- [Reactor Core 参考](https://projectreactor.io/docs/core/release/reference/)
- [示例代码](EXAMPLES.md#streaming-responses-使用-reactor-flux)

## 需要帮助？

如果迁移过程中遇到问题，请参考：
1. [REACTIVE_STREAMING.md](REACTIVE_STREAMING.md) - 详细的使用指南
2. [EXAMPLES.md](EXAMPLES.md) - 实际代码示例
3. [CHANGELOG.md](CHANGELOG.md) - 变更说明
