# Reactive Streaming with Project Reactor

本框架使用 [Project Reactor](https://projectreactor.io/) 的 `Flux` 来处理 LLM 的流式响应，而不是使用 Java 标准的 `Stream` API。

## 为什么使用 Reactor Flux？

相比 Java Stream API，Reactor Flux 提供了更多优势：

1. **非阻塞异步**: Flux 是完全非阻塞的，适合高并发场景
2. **背压支持**: 自动处理生产者和消费者速度不匹配的情况
3. **丰富的操作符**: 提供强大的流处理、转换、组合能力
4. **错误处理**: 更灵活的错误处理和重试机制
5. **可组合性**: 易于组合多个异步操作

## 基本用法

### 1. 简单的流式调用

```java
ChatModel chatModel = createChatModel();
ChatModelRequest request = ChatModelRequest.builder()
    .messages(messages)
    .build();

// 订阅并处理流式响应
Flux<ChatModelResponse> flux = chatModel.stream(request);
flux.subscribe(
    chunk -> {
        // 处理每个数据块
        String text = extractText(chunk);
        System.out.print(text);
    },
    error -> {
        // 处理错误
        System.err.println("Error: " + error.getMessage());
    },
    () -> {
        // 完成时调用
        System.out.println("\nStreaming completed!");
    }
);
```

### 2. 使用 StreamHandler

框架提供了 `StreamHandler` 接口和 `FluxStreamHandler` 工具类：

```java
StreamHandler<ChatModelResponse> handler = new StreamHandler<>() {
    @Override
    public void onStart() {
        System.out.println("Streaming started...");
    }
    
    @Override
    public void onChunk(ChatModelResponse chunk) {
        String text = extractText(chunk);
        System.out.print(text);
    }
    
    @Override
    public void onComplete() {
        System.out.println("\nStreaming completed!");
    }
    
    @Override
    public void onError(Throwable error) {
        System.err.println("Error: " + error.getMessage());
    }
};

// 订阅 Flux
Flux<ChatModelResponse> flux = chatModel.stream(request);
FluxStreamHandler.subscribe(flux, handler);
```

### 3. 阻塞等待完成

如果需要同步等待流完成：

```java
Flux<ChatModelResponse> flux = chatModel.stream(request);

// 阻塞直到流完成
FluxStreamHandler.subscribeBlocking(flux, handler);

// 或者收集所有结果
List<ChatModelResponse> results = flux.collectList().block();
```

## 高级用法

### 1. 流式转换

```java
Flux<ChatModelResponse> flux = chatModel.stream(request);

// 只提取文本内容
Flux<String> textFlux = flux
    .map(response -> extractText(response))
    .filter(text -> !text.isEmpty());

// 累积文本
Flux<String> accumulatedFlux = textFlux
    .scan("", (accumulated, chunk) -> accumulated + chunk);

// 订阅
accumulatedFlux.subscribe(System.out::println);
```

### 2. 错误处理和重试

```java
Flux<ChatModelResponse> flux = chatModel.stream(request)
    .retry(3)  // 失败时重试 3 次
    .timeout(Duration.ofSeconds(30))  // 30 秒超时
    .onErrorResume(error -> {
        // 错误时返回备用流
        log.error("Streaming failed, using fallback", error);
        return fallbackModel.stream(request);
    });
```

### 3. 限流控制

```java
Flux<ChatModelResponse> flux = chatModel.stream(request)
    .delayElements(Duration.ofMillis(100))  // 每个元素延迟 100ms
    .limitRate(10);  // 限制请求速率
```

### 4. 并行处理多个流

```java
Flux<ChatModelResponse> flux1 = model1.stream(request1);
Flux<ChatModelResponse> flux2 = model2.stream(request2);

// 合并多个流
Flux<ChatModelResponse> merged = Flux.merge(flux1, flux2);

// 依次处理（先完成 flux1，再处理 flux2）
Flux<ChatModelResponse> concatenated = Flux.concat(flux1, flux2);

// 打包成元组
Flux<Tuple2<ChatModelResponse, ChatModelResponse>> zipped = 
    Flux.zip(flux1, flux2);
```

### 5. 背压控制

```java
Flux<ChatModelResponse> flux = chatModel.stream(request)
    .onBackpressureBuffer(100)  // 缓冲 100 个元素
    .publishOn(Schedulers.boundedElastic());  // 在弹性线程池上发布
```

## 与 Observer 集成

```java
ModelObserver observer = new LoggingObserver();

Flux<ChatModelResponse> flux = chatModel.stream(request);

flux
    .doOnSubscribe(s -> observer.onStreamStart(request))
    .doOnNext(chunk -> observer.onStreamChunk(request, chunk))
    .doOnComplete(() -> observer.onStreamComplete(request))
    .doOnError(error -> observer.onCallError(request, error))
    .subscribe();
```

## 实际应用示例

### 实时聊天应用

```java
public class RealtimeChatService {
    private final ChatModel chatModel;
    
    public Flux<String> chat(String userId, String message) {
        // 获取对话历史
        List<Message> history = memory.get(userId);
        history.add(new UserMessage(message));
        
        ChatModelRequest request = ChatModelRequest.builder()
            .messages(history)
            .build();
        
        // 返回文本流
        return chatModel.stream(request)
            .map(this::extractText)
            .filter(text -> !text.isEmpty())
            .doOnComplete(() -> {
                // 保存完整响应到内存
                saveToMemory(userId, response);
            });
    }
    
    private String extractText(ChatModelResponse response) {
        if (response.getMessage() != null && 
            !response.getMessage().getContents().isEmpty()) {
            return response.getMessage()
                .getContents().get(0)
                .getValue().toString();
        }
        return "";
    }
}
```

### Spring WebFlux 集成

```java
@RestController
@RequestMapping("/api/chat")
public class ChatController {
    
    @Autowired
    private ChatModel chatModel;
    
    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamChat(@RequestBody ChatRequest request) {
        ChatModelRequest modelRequest = toModelRequest(request);
        
        return chatModel.stream(modelRequest)
            .map(this::extractText)
            .filter(text -> !text.isEmpty())
            .map(text -> ServerSentEvent.<String>builder()
                .data(text)
                .build())
            .onErrorResume(error -> 
                Flux.just(ServerSentEvent.<String>builder()
                    .event("error")
                    .data(error.getMessage())
                    .build())
            );
    }
}
```

### 结合 Function Calling

```java
public Flux<ChatModelResponse> chatWithTools(ChatModelRequest request) {
    return chatModel.stream(request)
        .expand(response -> {
            // 如果有函数调用，执行并继续流
            if (response.getFunctionCall().isPresent()) {
                FunctionCall call = response.getFunctionCall().get();
                ToolExecutionResult result = executor.execute(call);
                
                // 创建新请求包含工具结果
                ChatModelRequest newRequest = buildRequestWithToolResult(
                    request, call, result);
                
                // 继续流式调用
                return chatModel.stream(newRequest);
            }
            return Flux.empty();
        })
        .takeUntil(response -> response.getFunctionCall().isEmpty());
}
```

## 测试

使用 Reactor Test 进行测试：

```java
import reactor.test.StepVerifier;

@Test
public void testStreamingResponse() {
    Flux<ChatModelResponse> flux = chatModel.stream(request);
    
    StepVerifier.create(flux)
        .expectNextMatches(response -> response.getMessage() != null)
        .expectNextMatches(response -> !response.isLast())
        .expectNextMatches(ChatModelResponse::isLast)
        .verifyComplete();
}

@Test
public void testStreamingWithError() {
    Flux<ChatModelResponse> flux = chatModel.stream(invalidRequest);
    
    StepVerifier.create(flux)
        .expectError(InvalidRequestException.class)
        .verify();
}

@Test
public void testStreamingWithTimeout() {
    Flux<ChatModelResponse> flux = chatModel.stream(request)
        .timeout(Duration.ofSeconds(5));
    
    StepVerifier.create(flux)
        .expectNextCount(3)
        .verifyTimeout(Duration.ofSeconds(5));
}
```

## 性能优化建议

1. **使用适当的调度器**: 
   - `Schedulers.boundedElastic()` 用于 I/O 操作
   - `Schedulers.parallel()` 用于 CPU 密集型操作

2. **控制缓冲大小**: 使用 `onBackpressureBuffer()` 限制内存使用

3. **避免阻塞**: 尽量使用响应式操作符而不是 `.block()`

4. **合理使用 Hot vs Cold**: 
   - Cold Flux: 每个订阅者都会触发新的流
   - Hot Flux: 共享同一个流

5. **资源清理**: 使用 `using()` 或 `doFinally()` 确保资源释放

## 常见问题

### Q: 如何等待流完成？

```java
// 方式 1: 使用 block()（阻塞）
flux.blockLast();

// 方式 2: 收集结果
List<ChatModelResponse> results = flux.collectList().block();

// 方式 3: 使用 FluxStreamHandler（阻塞）
FluxStreamHandler.subscribeBlocking(flux, handler);
```

### Q: 如何取消流？

```java
Disposable subscription = flux.subscribe(...);
// 取消订阅
subscription.dispose();
```

### Q: 如何处理背压？

```java
flux
    .onBackpressureBuffer(100)  // 缓冲
    // 或
    .onBackpressureDrop()  // 丢弃
    // 或
    .onBackpressureLatest()  // 只保留最新
```

### Q: Flux 和 Stream 有什么区别？

| 特性 | Flux | Stream |
|-----|------|--------|
| 异步 | ✅ 完全异步 | ❌ 同步 |
| 背压 | ✅ 内置支持 | ❌ 无 |
| 错误处理 | ✅ 丰富的操作符 | ⚠️ 基础 |
| 组合性 | ✅ 高度可组合 | ⚠️ 有限 |
| 线程控制 | ✅ 灵活的调度器 | ❌ 无 |
| 取消 | ✅ 可取消 | ❌ 不可取消 |

## 参考资源

- [Project Reactor 官方文档](https://projectreactor.io/docs)
- [Reactor Core 参考指南](https://projectreactor.io/docs/core/release/reference/)
- [响应式编程入门](https://www.reactivemanifesto.org/)
- [Reactor by Example](https://www.infoq.com/articles/reactor-by-example/)
