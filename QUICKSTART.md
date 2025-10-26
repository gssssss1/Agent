# Quick Start Guide

## What is This?

This is a Java framework for building LLM (Large Language Model) applications. It provides vendor-neutral abstractions that let you write code once and use it with any LLM provider (OpenAI, Azure, Anthropic, etc.).

## Project Status

✅ **Core Abstractions**: All interfaces and base classes are complete
⏳ **Vendor Implementations**: Need to be implemented per provider

## File Overview

```
project/
├── README.md                   # Main documentation
├── EXAMPLES.md                 # Code examples
├── DESIGN_SUMMARY.md          # Architecture details
├── IMPLEMENTATION_NOTES.md    # Implementation status
├── QUICKSTART.md              # This file
├── pom.xml                    # Maven configuration
├── .gitignore                 # Git ignore rules
└── src/main/java/com/llmframework/
    ├── core/                  # Core interfaces
    │   ├── model/            # Model abstractions
    │   ├── result/           # Result types
    │   └── exception/        # Exception hierarchy
    ├── chat/                  # Chat models
    ├── embedding/             # Embedding models
    ├── image/                 # Image generation
    ├── prompt/                # Prompt templates
    ├── parser/                # Output parsing
    ├── client/                # HTTP clients
    ├── memory/                # Conversation memory
    ├── tool/                  # Function calling
    ├── observability/         # Monitoring
    └── retry/                 # Retry logic
```

## Key Concepts

### 1. Model Interface

All models implement `Model<REQ, RESP>`:

```java
public interface Model<REQ extends ModelRequest, RESP extends ModelResponse> {
    RESP call(REQ request);
    Stream<RESP> stream(REQ request);
    ModelMetadata getMetadata();
}
```

### 2. Request/Response Pattern

Every model type has its own request/response:
- `ChatModelRequest` → `ChatModelResponse`
- `EmbeddingModelRequest` → `EmbeddingModelResponse`
- `ImageModelRequest` → `ImageModelResponse`

### 3. Type-Safe Results

Responses contain typed results:
- `ChatModelResponse` → `TextResult`
- `EmbeddingModelResponse` → `EmbeddingResult`
- `ImageModelResponse` → `ImageResult`

### 4. Exception Hierarchy

All exceptions extend `ModelException` with `isRetryable()`:
- ✅ Retryable: `RateLimitException`, `TimeoutException`, `ModelUnavailableException`
- ❌ Not retryable: `AuthException`, `InvalidRequestException`

## Usage Pattern

### Basic Chat Example

```java
// 1. Create a model (implementation needed)
ChatModel chatModel = new OpenAIChatModel(config);

// 2. Prepare messages
List<Message> messages = List.of(
    new SystemMessage("You are a helpful assistant"),
    new UserMessage("What is Java?")
);

// 3. Call the model
ChatModelResponse response = chatModel.chat(messages);

// 4. Get the result
String answer = response.getMessage().getContents().get(0).getValue().toString();
```

### With All Features

```java
// Setup retry strategy
RetryStrategy retry = new ExponentialBackoffRetryStrategy(3, 1000, 2.0, 60000);

// Setup observability
ModelObserver observer = new LoggingObserver();

// Initialize memory
ConversationMemory memory = new InMemoryConversationMemory();

// Create model with all features
ChatModel chatModel = new OpenAIChatModel(config, retry, observer);

// Use it
String conversationId = "user-123";
List<Message> history = memory.get(conversationId, 4000);
history.add(new UserMessage("Hello!"));

ChatModelResponse response = chatModel.chat(history);
memory.add(conversationId, response.getMessage());
```

## What You Need to Implement

### 1. HTTP Client (Choose one)

```xml
<!-- OkHttp -->
<dependency>
    <groupId>com.squareup.okhttp3</groupId>
    <artifactId>okhttp</artifactId>
    <version>4.12.0</version>
</dependency>

<!-- Or Java 11+ HttpClient (built-in) -->
```

### 2. JSON Library (Choose one)

```xml
<!-- Jackson -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.0</version>
</dependency>

<!-- Or Gson -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

### 3. Vendor Implementation

Create a class like this:

```java
public class OpenAIChatModel implements ChatModel {
    private final String apiKey;
    private final HttpClient httpClient;
    private final RetryStrategy retryStrategy;
    private final ModelObserver observer;
    
    public OpenAIChatModel(ModelConfig config) {
        this.apiKey = config.getApiKey();
        this.httpClient = createHttpClient();
        this.retryStrategy = config.getRetryStrategy();
        this.observer = config.getObserver();
    }
    
    @Override
    public ChatModelResponse call(ChatModelRequest request) {
        // 1. Convert request to OpenAI format
        // 2. Make HTTP call
        // 3. Handle response
        // 4. Handle errors and retries
        // 5. Notify observers
        // 6. Return ChatModelResponse
    }
    
    @Override
    public Stream<ChatModelResponse> stream(ChatModelRequest request) {
        // Similar but for streaming
    }
    
    @Override
    public ModelMetadata getMetadata() {
        return new ModelMetadata("gpt-4", "1.0", "OpenAI", capabilities);
    }
}
```

## Building the Project

```bash
# Compile
mvn clean compile

# Run tests (when you add them)
mvn test

# Package
mvn package

# Install to local Maven repo
mvn install
```

## Testing Without Implementation

You can write application code now using mocks:

```java
// Create a mock for testing
ChatModel mockModel = mock(ChatModel.class);
when(mockModel.chat(any())).thenReturn(mockResponse);

// Your application code works with the interface
MyApp app = new MyApp(mockModel);
app.chat("Hello!");
```

## Adding to Your Project

Once published to Maven Central (or your private repo):

```xml
<dependency>
    <groupId>com.llmframework</groupId>
    <artifactId>llm-core</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Architecture Benefits

✅ **Write once, use everywhere**: Code works with any LLM provider
✅ **Type-safe**: Compile-time checking
✅ **Testable**: Mock any component
✅ **Observable**: Built-in monitoring
✅ **Resilient**: Automatic retries
✅ **Flexible**: Plug in your own implementations

## Next Steps

1. **Read the Design**: See `DESIGN_SUMMARY.md`
2. **Check Examples**: See `EXAMPLES.md`
3. **Implement a Provider**: Start with OpenAI
4. **Add Tests**: Use JUnit and Mockito
5. **Deploy**: Publish to your Maven repo

## Support

- Check `README.md` for overview
- Check `EXAMPLES.md` for code samples
- Check `DESIGN_SUMMARY.md` for architecture
- Check `IMPLEMENTATION_NOTES.md` for status

## Example Implementations to Build

Priority order:

1. **OpenAI Chat Model** (most common)
2. **OpenAI Embedding Model** (for RAG apps)
3. **Message Implementations** (SystemMessage, UserMessage, etc.)
4. **Model Registry** (with routing)
5. **Output Parsers** (JSON, XML)
6. **Additional Memory Backends** (Redis, Database)

## Common Patterns

### Pattern 1: Chat with Memory

```java
ConversationMemory memory = new InMemoryConversationMemory();
ChatModel model = createModel();

public String chat(String userId, String message) {
    String convId = "user-" + userId;
    List<Message> history = memory.get(convId, 4000);
    history.add(new UserMessage(message));
    
    ChatModelResponse response = model.chat(history);
    memory.add(convId, response.getMessage());
    
    return extractText(response);
}
```

### Pattern 2: Streaming Response

```java
model.stream(request).forEach(chunk -> {
    String text = extractText(chunk);
    System.out.print(text);
});
```

### Pattern 3: Function Calling

```java
ChatModelRequest request = ChatModelRequest.builder()
    .messages(messages)
    .functions(toolDefinitions)
    .build();

ChatModelResponse response = model.call(request);
if (response.getFunctionCall().isPresent()) {
    FunctionCall call = response.getFunctionCall().get();
    ToolExecutionResult result = executor.execute(call);
    // Continue conversation with result
}
```

### Pattern 4: Multi-Provider Fallback

```java
ModelRegistry registry = new SimpleModelRegistry();
registry.register("primary", openAiModel);
registry.register("fallback", azureModel);

try {
    ChatModel model = registry.getModel("primary", ChatModel.class);
    return model.chat(messages);
} catch (ModelException e) {
    if (e.isRetryable()) {
        ChatModel fallback = registry.getModel("fallback", ChatModel.class);
        return fallback.chat(messages);
    }
    throw e;
}
```

## Tips

1. **Start Simple**: Implement one model type first
2. **Test Early**: Write tests as you implement
3. **Use Builders**: The builder pattern makes requests easy
4. **Log Everything**: Use the LoggingObserver
5. **Handle Errors**: All LLM calls can fail
6. **Monitor Costs**: Use CostCalculator

## Questions?

- Is the API intuitive? ✅
- Is it type-safe? ✅
- Can I switch providers easily? ✅
- Can I add custom features? ✅
- Is it production-ready? ✅ (once implemented)

Happy coding! 🚀
