# Modern Java LLM Framework - Architecture Design Document v2.0

## 概述 (Overview)

这是一个现代化的 Java LLM 应用开发框架，采用响应式、函数式和声明式设计理念，提供优雅的 API 和强大的扩展能力。

This is a modern Java LLM application development framework that adopts reactive, functional, and declarative design principles, providing elegant APIs and powerful extensibility.

## 核心设计理念 (Core Design Principles)

### 1. 响应式优先 (Reactive-First)
- 基于 Project Reactor，原生支持异步和背压
- 所有 I/O 操作返回 `Mono` 或 `Flux`
- 支持非阻塞、事件驱动的编程模型

### 2. 函数式编程 (Functional Programming)
- 充分利用 Java 21+ 特性（Records, Sealed Classes, Pattern Matching, Virtual Threads）
- 不可变数据结构
- 函数组合和高阶函数

### 3. 声明式配置 (Declarative Configuration)
- Fluent API 和 Builder 模式
- DSL 风格的配置
- 类型安全的编译时检查

### 4. 类型安全 (Type Safety)
- Sealed interfaces 限制实现
- 泛型确保编译时类型检查
- Pattern matching 简化类型判断

### 5. 零拷贝流式 (Zero-Copy Streaming)
- 高效的流式处理
- 最小化内存占用
- 支持大规模数据处理

### 6. 可观测性内置 (Built-in Observability)
- 开箱即用的监控
- 分布式追踪
- 成本管理

### 7. 插件化架构 (Pluggable Architecture)
- 基于 SPI 的扩展机制
- 拦截器链模式
- 多提供商适配器

## 技术栈 (Tech Stack)

| 技术 | 版本 | 用途 |
|------|------|------|
| Java | 21+ | 编程语言，支持最新特性 |
| Project Reactor | 3.6.x | 响应式编程核心 |
| Jackson | 2.16.x | JSON 处理 |
| Micrometer | 1.12.x | 指标收集 |
| Caffeine | 3.1.x | 高性能缓存 |
| Resilience4j | 2.2.x | 容错和限流 |
| SLF4J | 2.0.x | 日志抽象层 |

## 整体架构 (Overall Architecture)

```
┌─────────────────────────────────────────────────────────┐
│                     Application Layer                    │
│                  (User Application Code)                 │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                    Fluent API / DSL                      │
│         (ChatModel, EmbeddingModel, ImageModel)          │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   Framework Core Layer                   │
│  ┌──────────────┬───────────────┬───────────────────┐   │
│  │ Model Facade │ Chain Builder │ Prompt Engine     │   │
│  └──────────────┴───────────────┴───────────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                Reactive Engine Layer                     │
│  ┌──────────────┬───────────────┬───────────────────┐   │
│  │ Reactive     │ Semantic      │ Interceptor       │   │
│  │ Model Core   │ Cache         │ Chain             │   │
│  └──────────────┴───────────────┴───────────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                  Capability Layer                        │
│  ┌──────────────┬───────────────┬───────────────────┐   │
│  │ Chat         │ Embedding     │ Image             │   │
│  │ Tool Exec    │ RAG Pipeline  │ Memory Mgmt       │   │
│  └──────────────┴───────────────┴───────────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│               Infrastructure Layer                       │
│  ┌──────────────┬───────────────┬───────────────────┐   │
│  │ HTTP Client  │ Retry/Circuit │ Observability     │   │
│  │ Connection   │ Breaker       │ Cost Tracker      │   │
│  │ Pool         │ Rate Limiter  │ Metrics           │   │
│  └──────────────┴───────────────┴───────────────────┘   │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────▼────────────────────────────────────┐
│                   Adapter Layer                          │
│  ┌──────────────┬───────────────┬───────────────────┐   │
│  │ OpenAI       │ Azure OpenAI  │ Anthropic         │   │
│  │ Adapter      │ Adapter       │ Adapter           │   │
│  └──────────────┴───────────────┴───────────────────┘   │
└─────────────────────────────────────────────────────────┘
```

## 核心接口设计 (Core Interface Design)

### 1. ReactiveModel<I, O>

响应式模型的核心接口：

```java
public interface ReactiveModel<I extends ModelInput, O extends ModelOutput> {
    Mono<O> call(I input);
    Flux<StreamingChunk<O>> stream(I input);
    Flux<O> callBatch(Publisher<I> inputs);
    O callSync(I input);  // 便捷的同步方法
    ModelCapabilities capabilities();
    ModelMetadata metadata();
}
```

### 2. Message System

支持多模态的消息系统：

```java
public sealed interface Message
        permits TextMessage, MultiModalMessage, ToolMessage, 
                SystemMessage, AssistantMessage {
    MessageRole role();
    String content();
    String name();
    int estimateTokens();
}
```

### 3. ChatModel

聊天模型接口：

```java
public interface ChatModel extends ReactiveModel<ChatInput, ChatOutput> {
    Mono<String> chat(String message);
    Mono<String> chat(List<Message> messages);
    <T> Mono<T> chatStructured(String message, Class<T> outputType);
    Flux<StreamingChunk<ChatOutput>> streamWithAccumulation(ChatInput input);
}
```

## 关键特性 (Key Features)

### 1. 工具调用 (Tool/Function Calling)

```java
Tool weatherTool = Tool.builder("get_weather")
    .description("Get current weather")
    .parameter("location", String.class, "City name")
    .function(args -> {
        String city = (String) args.get("location");
        return "Weather in " + city;
    })
    .build();

ToolChain chain = ToolChain.builder(chatModel, toolExecutor)
    .maxIterations(5)
    .build();
```

### 2. Prompt 模板引擎 (Prompt Template Engine)

```java
PromptTemplate template = PromptTemplates.chat()
    .system("You are a ${role}")
    .user("${task}")
    .build();

ChatInput input = template.toChatInput(Map.of(
    "role", "helpful assistant",
    "task", "Explain quantum computing"
));
```

### 3. RAG Pipeline

```java
RAGChain ragChain = RAGChain.builder(chatModel, retriever)
    .topK(3)
    .includeSourceCitations(true)
    .build();

ragChain.call("Tell me about programming languages")
    .subscribe(System.out::println);
```

### 4. 对话记忆 (Conversation Memory)

```java
ConversationMemory memory = new InMemoryConversationMemory(100);

// 添加消息
memory.add(conversationId, Message.user("Hello")).block();

// 获取历史（带窗口策略）
List<Message> history = memory.get(
    conversationId,
    WindowStrategy.sliding(1000)
).block();
```

### 5. 智能缓存 (Smart Caching)

```java
// 多层缓存
ModelCache cache = new TieredCache(l1Cache, l2Cache);

// 语义缓存
SemanticCache semanticCache = new VectorSemanticCache(
    embeddingModel,
    vectorStore,
    fallbackCache
);
```

### 6. 成本追踪 (Cost Tracking)

```java
CostTracker costTracker = new InMemoryCostTracker();

ChatModel model = OpenAIChatModel.builder()
    .apiKey(apiKey)
    .costTracker(costTracker)
    .build();

CostStatistics stats = costTracker.getStatistics().block();
System.out.println("Total cost: $" + stats.totalCost());
```

### 7. 拦截器链 (Interceptor Chain)

```java
ChatModel model = OpenAIChatModel.builder()
    .apiKey(apiKey)
    .interceptor(new LoggingInterceptor())
    .interceptor(new MetricsInterceptor(registry, "gpt-4"))
    .interceptor(new CacheInterceptor(cache, "gpt-4", ttl))
    .build();
```

## 包结构 (Package Structure)

```
com.llmframework/
├── core/
│   ├── model/          # Core model interfaces
│   │   ├── ReactiveModel.java
│   │   ├── ModelInput.java
│   │   ├── ModelOutput.java
│   │   ├── ModelOptions.java
│   │   ├── ModelCapabilities.java
│   │   ├── ModelMetadata.java
│   │   ├── ModelType.java
│   │   └── StreamingChunk.java
│   ├── message/        # Message system
│   │   ├── Message.java
│   │   ├── MessageRole.java
│   │   ├── TextMessage.java
│   │   ├── SystemMessage.java
│   │   ├── AssistantMessage.java
│   │   ├── ToolMessage.java
│   │   ├── MultiModalMessage.java
│   │   └── Content.java
│   └── usage/          # Token counting
│       ├── Usage.java
│       ├── TokenEstimate.java
│       └── TokenCounter.java
├── chat/               # Chat model
│   ├── ChatModel.java
│   ├── ChatInput.java
│   ├── ChatOutput.java
│   └── FinishReason.java
├── embedding/          # Embedding model
│   ├── EmbeddingModel.java
│   ├── EmbeddingInput.java
│   └── EmbeddingOutput.java
├── image/              # Image model
│   ├── ImageModel.java
│   ├── ImageInput.java
│   └── ImageOutput.java
├── prompt/             # Prompt templates
├── tool/               # Tool/function calling
│   ├── Tool.java
│   ├── ToolCall.java
│   ├── ToolChoice.java
│   ├── ToolExecutor.java
│   ├── ToolResult.java
│   └── ToolChain.java
├── format/             # Response formats
│   ├── ResponseFormat.java
│   ├── JsonSchema.java
│   └── JsonSchemaGenerator.java
├── cache/              # Caching layer
├── memory/             # Conversation memory
├── rag/                # RAG components
├── interceptor/        # Middleware
├── cost/               # Cost tracking
│   └── PricingInfo.java
├── exception/          # Exception handling
├── retry/              # Retry strategies
├── ratelimit/          # Rate limiting
├── client/             # HTTP client
├── adapter/            # Provider adapters
│   └── openai/         # OpenAI implementation
├── util/               # Utilities
│   └── JsonUtils.java
└── example/            # Usage examples
    └── BasicExample.java
```

## 设计决策 (Design Decisions)

### 1. 为什么选择 Reactive？

- **非阻塞 I/O**: 更高的并发能力和资源利用率
- **背压支持**: 自动处理快慢生产者-消费者问题
- **组合性**: 强大的操作符链式组合
- **错误处理**: 统一的错误传播和处理机制

### 2. 为什么使用 Java Records？

- **不可变性**: 线程安全，减少错误
- **简洁性**: 减少样板代码
- **值语义**: 自动实现 equals/hashCode/toString
- **模式匹配**: 配合 Java 21 的模式匹配特性

### 3. 为什么使用 Sealed Interfaces？

- **类型安全**: 限制实现，编译时检查
- **穷举性**: Pattern matching 可以检查所有情况
- **封装性**: 框架内部控制实现

### 4. 缓存策略

- **L1 缓存 (Caffeine)**: 进程内，低延迟，小容量
- **L2 缓存 (Redis)**: 分布式，持久化，大容量
- **语义缓存**: 基于向量相似度的智能缓存

### 5. 错误处理

- **可重试异常**: 网络错误、超时、限流
- **不可重试异常**: 认证错误、无效请求
- **指数退避**: 智能重试策略

## 性能优化 (Performance Optimization)

### 1. 连接池
- HTTP 客户端连接复用
- 可配置的连接数和超时

### 2. 批量处理
- 自动批量请求合并
- 并行处理提高吞吐量

### 3. 流式处理
- 零拷贝流式传输
- 最小化内存占用

### 4. 缓存
- 多层缓存策略
- 语义相似度缓存

## 可扩展性 (Extensibility)

### 1. 适配器模式
- 统一的接口抽象
- 支持多个 LLM 提供商

### 2. 拦截器链
- 横切关注点分离
- 可组合的中间件

### 3. SPI 机制
- 插件化扩展
- 运行时发现和加载

## 安全性 (Security)

### 1. API 密钥管理
- 环境变量
- 密钥管理服务集成

### 2. 输入验证
- 长度限制
- Prompt 注入检测

### 3. 输出过滤
- 敏感信息检测
- 内容审核

### 4. 配额管理
- 用户级别限流
- 成本控制

## 测试策略 (Testing Strategy)

### 1. 单元测试
- 使用 Mock 对象
- JUnit 5 + Reactor Test

### 2. 集成测试
- 真实 API 调用（可选）
- 测试环境隔离

### 3. 性能测试
- 基准测试
- 负载测试

## 未来扩展 (Future Extensions)

### 1. 更多模型支持
- Google Gemini
- Meta Llama
- 国产大模型（通义千问、文心一言等）

### 2. 高级特性
- 模型路由和负载均衡
- A/B 测试支持
- 模型微调集成

### 3. 企业功能
- 审计日志
- 权限控制
- 多租户支持

### 4. 生态集成
- Spring Boot Starter
- Quarkus Extension
- Kubernetes Operator

## 最佳实践 (Best Practices)

### 1. 使用响应式 API

```java
// 推荐
chatModel.call(input)
    .subscribe(output -> processOutput(output));

// 避免（除非在测试中）
ChatOutput output = chatModel.callSync(input);
```

### 2. 合理配置缓存

```java
// L1: 高频访问，小容量
Cache<CacheKey, ModelOutput> l1 = Caffeine.newBuilder()
    .maximumSize(100)
    .expireAfterWrite(Duration.ofMinutes(10))
    .build();

// L2: 低频访问，大容量
ModelCache cache = new TieredCache(l1, l2);
```

### 3. 使用 Prompt 模板

```java
// 推荐：使用模板
PromptTemplate template = PromptTemplates.chat()
    .system("You are a ${role}")
    .user("${query}")
    .build();

// 避免：硬编码
String hardcodedPrompt = "You are a helpful assistant...";
```

### 4. 成本控制

```java
// 预估成本
TokenEstimate estimate = input.estimateTokens();
BigDecimal estimatedCost = pricing.calculateCost(
    Usage.of(estimate.promptTokens(), estimate.maxCompletionTokens())
);

if (estimatedCost.compareTo(MAX_COST) > 0) {
    throw new IllegalArgumentException("Cost too high");
}
```

## 版本历史 (Version History)

- **v1.0.0-SNAPSHOT**: 初始版本，核心功能实现
  - Reactive model interfaces
  - Chat/Embedding/Image models
  - Tool calling support
  - Basic caching

## 参考资料 (References)

- [Project Reactor Documentation](https://projectreactor.io/docs)
- [OpenAI API Documentation](https://platform.openai.com/docs)
- [Java 21 Features](https://openjdk.org/projects/jdk/21/)
- [Reactive Streams Specification](https://www.reactive-streams.org/)

## 许可证 (License)

Apache License 2.0

---

**文档版本**: v2.0  
**最后更新**: 2024  
**维护者**: 开发团队
