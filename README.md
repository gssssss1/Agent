# Modern Java LLM Framework

A modern, reactive Java framework for LLM applications built with Java 21+, Project Reactor, and functional programming principles.

## Features

### Core Architecture
- **Reactive-First Design**: Built on Project Reactor for non-blocking, async operations
- **Functional Programming**: Leverages Java 21+ features (Records, Sealed Classes, Pattern Matching)
- **Type-Safe**: Compile-time type checking with generics and sealed interfaces
- **Declarative Configuration**: Fluent API and Builder patterns for elegant configuration

### Key Capabilities
- **Multi-Model Support**: Chat, Embedding, Image generation models
- **Streaming Responses**: Native support for streaming LLM outputs
- **Tool/Function Calling**: Annotation-based tool definitions with automatic execution
- **RAG Pipeline**: Built-in Retrieval-Augmented Generation support
- **Prompt Templates**: Template engine with variable substitution and few-shot learning
- **Conversation Memory**: Multiple window strategies (sliding, summary, priority)
- **Smart Caching**: Multi-tier caching with semantic similarity matching
- **Cost Tracking**: Automatic token usage and cost monitoring
- **Observability**: Built-in metrics, tracing, and logging

### Enterprise Features
- **Retry Strategies**: Configurable exponential backoff and circuit breakers
- **Rate Limiting**: Token bucket algorithm for API throttling
- **Error Handling**: Comprehensive exception hierarchy with retry logic
- **Interceptor Chain**: Middleware pattern for cross-cutting concerns
- **Multi-Provider**: Pluggable adapters for OpenAI, Azure, Anthropic, etc.

## Quick Start

### Maven Dependency

```xml
<dependency>
    <groupId>com.llmframework</groupId>
    <artifactId>modern-java-llm-framework</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Basic Usage

```java
// Create a chat model
ChatModel chatModel = OpenAIChatModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName("gpt-4")
    .build();

// Simple chat
String response = chatModel.chat("What is the capital of France?")
    .block();

// Streaming response
chatModel.stream(ChatInput.of("Write a short poem"))
    .subscribe(chunk -> System.out.print(chunk.output().content()));

// Structured output
record Person(String name, int age, String occupation) {}

Person person = chatModel.chatStructured(
    "Extract: John is a 30-year-old software engineer",
    Person.class
).block();
```

### Advanced Features

#### Tool Calling

```java
// Define a tool with annotation
public class WeatherService {
    @ToolDefinition(description = "Get current weather")
    public String getWeather(
        @ToolParameter(description = "City name") String city) {
        return "Weather in " + city + " is sunny, 22°C";
    }
}

// Register and use
ToolExecutor executor = new DefaultToolExecutor();
executor.register(new WeatherService());

ToolChain chain = ToolChain.builder(chatModel, executor)
    .maxIterations(5)
    .build();

chain.execute(ChatInput.builder()
    .message("What's the weather in Paris?")
    .tool(weatherTool)
    .build())
    .subscribe(System.out::println);
```

#### RAG Pipeline

```java
// Setup embedding and vector store
EmbeddingModel embeddingModel = OpenAIEmbeddingModel.builder()
    .apiKey(System.getenv("OPENAI_API_KEY"))
    .modelName("text-embedding-3-small")
    .build();

VectorStore vectorStore = new InMemoryVectorStore(embeddingModel);
vectorStore.add(documents).block();

// Create RAG chain
Retriever retriever = new VectorStoreRetriever(vectorStore, embeddingModel);

RAGChain ragChain = RAGChain.builder(chatModel, retriever)
    .topK(3)
    .includeSourceCitations(true)
    .build();

ragChain.call("Tell me about programming languages")
    .subscribe(System.out::println);
```

#### Conversation Memory

```java
ConversationMemory memory = new InMemoryConversationMemory(100);
String conversationId = "user-123";

// Add messages
memory.add(conversationId, Message.user("Hello")).block();
memory.add(conversationId, Message.assistant("Hi!")).block();

// Get history with window strategy
List<Message> history = memory.get(
    conversationId,
    WindowStrategy.sliding(1000)
).block();
```

#### Caching

```java
// Multi-tier cache
Cache<CacheKey, CachedEntry> l1 = Caffeine.newBuilder()
    .maximumSize(100)
    .build();

ModelCache cache = new TieredCache(l1, l2);

ChatModel model = OpenAIChatModel.builder()
    .apiKey(apiKey)
    .cache(cache)
    .build();
```

#### Cost Tracking

```java
CostTracker costTracker = new InMemoryCostTracker();

ChatModel model = OpenAIChatModel.builder()
    .apiKey(apiKey)
    .costTracker(costTracker)
    .build();

// Get statistics
CostStatistics stats = costTracker.getStatistics().block();
System.out.println("Total cost: $" + stats.totalCost());
System.out.println("Average per request: $" + stats.averageCostPerRequest());
```

## Architecture

### Package Structure

```
com.llmframework/
├── core/
│   ├── model/          # Core model interfaces
│   ├── message/        # Message system
│   └── usage/          # Token counting
├── chat/               # Chat model implementation
├── embedding/          # Embedding model
├── image/              # Image model
├── prompt/             # Prompt templates
├── tool/               # Tool/function calling
├── format/             # Response formats
├── cache/              # Caching layer
├── memory/             # Conversation memory
├── rag/                # RAG components
├── interceptor/        # Middleware
├── cost/               # Cost tracking
├── exception/          # Exception handling
├── retry/              # Retry strategies
├── ratelimit/          # Rate limiting
├── client/             # HTTP client
├── adapter/            # Provider adapters
│   └── openai/        # OpenAI implementation
└── util/               # Utilities
```

### Design Principles

1. **Reactive First**: All I/O operations return `Mono` or `Flux`
2. **Immutable Data**: Use Java Records for all data classes
3. **Type Safety**: Sealed interfaces and compile-time checks
4. **Extensibility**: Interface-first design with SPI mechanism
5. **Observability**: Built-in metrics and tracing
6. **Fault Tolerance**: Configurable retry and circuit breaker
7. **Performance**: Connection pooling and multi-tier caching

## Requirements

- Java 21 or higher
- Maven 3.8+

## Building

```bash
mvn clean install
```

## Testing

```bash
mvn test
```

## License

Apache License 2.0

## Contributing

Contributions are welcome! Please read our contributing guidelines.

## Documentation

Full documentation is available at [docs/](docs/).

## Examples

See [examples/](examples/) directory for complete working examples.
