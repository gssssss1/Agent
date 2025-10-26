# Java LLM Framework - Core Abstractions

A comprehensive Java framework for building Large Language Model (LLM) applications with vendor-neutral abstractions.

## Overview

This framework provides a unified, type-safe abstraction layer for working with different LLM providers (OpenAI, Azure, Anthropic, etc.). It follows interface-first design principles and supports various advanced features like streaming, function calling, conversation memory, and observability.

## Core Design Principles

- **Interface-First**: All core components are defined as interfaces for easy extension and testing
- **Type Safety**: Uses Java generics to ensure compile-time type safety
- **Vendor Neutral**: Abstraction layer doesn't depend on specific vendor implementations
- **Composability**: Components can be used independently or composed together
- **Observability**: Built-in support for monitoring, logging, tracing, and cost calculation

## Architecture

### Layer Structure

1. **Application Layer**: Business code written by developers
2. **Framework Core Layer**: Core abstraction interfaces
3. **Client Layer**: Common logic for model invocation
4. **Observability Layer**: Monitoring, logging, cost calculation
5. **Vendor Adapter Layer**: Specific vendor implementations

## Key Components

### 1. Core Model Abstractions

- `Model<REQ, RESP>`: Base interface for all models
- `ModelRequest`: Request interface with options and instructions
- `ModelResponse<T>`: Response interface with results and metadata
- `ModelOptions`: Configuration options (temperature, max tokens, etc.)
- `ModelMetadata`: Model information (name, version, capabilities)

### 2. Result Types

- `Result`: Base interface for all results
- `TextResult`: Text output
- `ImageResult`: Image output (URL or Base64)
- `EmbeddingResult`: Vector embeddings
- `ResultMetadata`: Token usage, duration, and other metadata

### 3. Specific Model Types

#### Chat Model
```java
ChatModel chatModel = ...;
ChatModelResponse response = chatModel.chat(messages);
```

#### Embedding Model
```java
EmbeddingModel embeddingModel = ...;
List<Float> embedding = embeddingModel.embed("Hello, world!");
```

#### Image Model
```java
ImageModel imageModel = ...;
List<String> imageUrls = imageModel.generate("A beautiful sunset");
```

### 4. Prompt Management

- `Prompt`: Renderable prompt with variables
- `PromptTemplate`: Template for creating prompts
- `Message`: Chat message with role and content
- `Content`: Multi-modal content (text, image, audio, video)

### 5. Output Processing

- `OutputParser<T>`: Parse LLM output to structured objects
- `StreamHandler<T>`: Handle streaming responses

### 6. Tool Execution

- `Tool`: Function/tool interface
- `ToolExecutor`: Execute function calls
- `ToolChain`: Orchestrate multiple tool executions
- `FunctionCall` / `FunctionDefinition`: Function calling support

### 7. Conversation Memory

- `ConversationMemory`: Store and retrieve conversation history
- Support for token-based windowing
- Multiple storage backends (in-memory, Redis, database)

### 8. Observability

- `ModelObserver`: Monitor model calls (start, success, error, streaming)
- `CostCalculator`: Calculate API usage costs
- `PricingInfo`: Model pricing information

### 9. Retry Strategy

- `RetryStrategy`: Configurable retry logic
- `ExponentialBackoffRetryStrategy`: Exponential backoff
- `FixedDelayRetryStrategy`: Fixed delay
- `NoRetryStrategy`: No retry

### 10. Exception Handling

- `ModelException`: Base exception class
- `RateLimitException`: Rate limit errors (retryable)
- `TimeoutException`: Timeout errors (retryable)
- `AuthException`: Authentication errors (not retryable)
- `InvalidRequestException`: Invalid request errors (not retryable)
- `ModelUnavailableException`: Model unavailable errors (retryable)

## Package Structure

```
com.llmframework
├── core
│   ├── model         # Core model interfaces
│   ├── result        # Result types and metadata
│   └── exception     # Exception hierarchy
├── chat              # Chat model interfaces
├── embedding         # Embedding model interfaces
├── image             # Image model interfaces
├── prompt            # Prompt management
├── parser            # Output parsing
├── client            # Model client and registry
├── memory            # Conversation memory
├── tool              # Tool execution
├── observability     # Monitoring and cost calculation
└── retry             # Retry strategies
```

## Usage Examples

### Basic Chat Example

```java
// Create a chat model (implementation specific)
ChatModel chatModel = new OpenAIChatModel(config);

// Create messages
List<Message> messages = List.of(
    new SystemMessage("You are a helpful assistant"),
    new UserMessage("What is the capital of France?")
);

// Call the model
ChatModelResponse response = chatModel.chat(messages);
System.out.println(response.getMessage().getContent());
```

### Streaming Example (使用 Reactor Flux)

```java
// 订阅流式响应
Flux<ChatModelResponse> flux = model.stream(request);
flux.subscribe(
    chunk -> System.out.print(extractText(chunk)),
    error -> System.err.println("Error: " + error),
    () -> System.out.println("\nDone!")
);

// 或使用 StreamHandler
FluxStreamHandler.subscribe(flux, handler);
```

详见 [REACTIVE_STREAMING.md](REACTIVE_STREAMING.md) 了解更多关于 Reactor Flux 的用法。

### With Retry Strategy

```java
RetryStrategy retryStrategy = new ExponentialBackoffRetryStrategy(3, 1000, 2.0, 60000);
// Configure model with retry strategy
```

### With Observer

```java
ModelObserver observer = new LoggingObserver();
// Register observer with model
```

## Requirements

- Java 21+
- Maven or Gradle

## Dependencies

Core dependencies:
- **SLF4J 2.0.9** - Logging abstraction
- **Reactor Core 3.6.0** - Reactive streams with Flux/Mono
- **JUnit 5** (test) - Unit testing

Optional dependencies for implementations:
- Jackson or Gson - JSON processing
- OkHttp - HTTP client
- Micrometer - Metrics collection
- OpenTelemetry - Distributed tracing
- Redis client - Redis memory backend
- Reactor Test (test) - Testing reactive streams

## Design Patterns

- **Strategy Pattern**: RetryStrategy, routing strategies
- **Observer Pattern**: ModelObserver
- **Factory Pattern**: Model creation
- **Builder Pattern**: Request object construction
- **Adapter Pattern**: Vendor API adapters
- **Template Method**: Abstract base classes
- **Chain of Responsibility**: Interceptor chains

## Future Extensions

- Support for more model types (audio, video)
- Advanced prompt engineering tools
- Model comparison and A/B testing
- Caching layer
- Rate limiting
- Circuit breaker pattern
- Model versioning and rollback

## License

[Add your license here]

## Contributing

[Add contribution guidelines here]
