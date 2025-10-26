# Java LLM Framework - Usage Examples

This document provides comprehensive examples of how to use the Java LLM Framework.

## Table of Contents

1. [Basic Usage](#basic-usage)
2. [Prompt Templates](#prompt-templates)
3. [Streaming Responses](#streaming-responses)
4. [Conversation Memory](#conversation-memory)
5. [Function Calling](#function-calling)
6. [Observability](#observability)
7. [Retry Strategies](#retry-strategies)
8. [Output Parsing](#output-parsing)

## Basic Usage

### Simple Chat Example

```java
import com.llmframework.chat.*;
import com.llmframework.prompt.*;

// Assuming you have a ChatModel implementation
ChatModel chatModel = createYourChatModel();

// Create messages
List<Message> messages = List.of(
    createSystemMessage("You are a helpful assistant"),
    createUserMessage("What is the capital of France?")
);

// Call the model
ChatModelResponse response = chatModel.chat(messages);

// Get the result
String answer = response.getMessage().getContents().get(0).getValue().toString();
System.out.println(answer); // "Paris"
```

### Embedding Example

```java
import com.llmframework.embedding.*;

EmbeddingModel embeddingModel = createYourEmbeddingModel();

// Embed single text
List<Float> embedding = embeddingModel.embed("Hello, world!");
System.out.println("Embedding dimension: " + embedding.size());

// Embed multiple texts
EmbeddingModelRequest request = EmbeddingModelRequest.builder()
    .texts(List.of("Text 1", "Text 2", "Text 3"))
    .dimensions(1536)
    .build();
    
EmbeddingModelResponse response = embeddingModel.call(request);
List<List<Float>> embeddings = response.getEmbeddings();
```

### Image Generation Example

```java
import com.llmframework.image.*;

ImageModel imageModel = createYourImageModel();

// Generate single image
List<String> imageUrls = imageModel.generate("A beautiful sunset over the ocean");
System.out.println("Generated image: " + imageUrls.get(0));

// Generate multiple images with options
ImageModelRequest request = ImageModelRequest.builder()
    .prompt("A cute cat wearing sunglasses")
    .n(3)
    .size("1024x1024")
    .quality("hd")
    .build();
    
ImageModelResponse response = imageModel.call(request);
List<String> images = response.getImageUrls();
```

## Prompt Templates

### Using Simple Prompt Templates

```java
import com.llmframework.prompt.*;
import com.llmframework.prompt.impl.*;

// Create a template
PromptTemplate template = new SimplePromptTemplate(
    "You are a {role}. Please help the user with {task}."
);

// Create variables
Map<String, Object> variables = Map.of(
    "role", "senior software engineer",
    "task", "debugging their code"
);

// Validate variables
if (template.validate(variables)) {
    Prompt prompt = template.create(variables);
    String rendered = prompt.getContent();
    System.out.println(rendered);
    // Output: "You are a senior software engineer. Please help the user with debugging their code."
}
```

### Multi-Modal Messages

```java
import com.llmframework.prompt.*;

// Create a message with text and image
Message message = new MultiModalMessage(
    MessageRole.USER,
    List.of(
        new TextContent("What's in this image?"),
        new ImageUrlContent("https://example.com/image.jpg")
    )
);

List<Message> messages = List.of(message);
ChatModelResponse response = chatModel.chat(messages);
```

## Streaming Responses (使用 Reactor Flux)

本框架使用 Project Reactor 的 `Flux` 来处理流式响应。详细信息请参见 [REACTIVE_STREAMING.md](REACTIVE_STREAMING.md)。

### Basic Streaming

```java
import com.llmframework.parser.*;
import reactor.core.publisher.Flux;

ChatModelRequest request = createYourRequest();

// 使用 Reactor Flux
Flux<ChatModelResponse> flux = chatModel.stream(request);
flux.subscribe(
    chunk -> {
        // 处理每个数据块
        if (chunk.getMessage() != null && !chunk.getMessage().getContents().isEmpty()) {
            String content = chunk.getMessage().getContents().get(0).getValue().toString();
            System.out.print(content);
        }
    },
    error -> {
        // 处理错误
        System.err.println("Error: " + error.getMessage());
    },
    () -> {
        // 完成
        System.out.println("\nStreaming completed!");
    }
);
```

### Streaming with Handler

```java
import com.llmframework.parser.*;

StreamHandler<ChatModelResponse> handler = new StreamHandler<>() {
    private StringBuilder fullResponse = new StringBuilder();
    
    @Override
    public void onStart() {
        System.out.println("Streaming started...");
    }
    
    @Override
    public void onChunk(ChatModelResponse chunk) {
        String content = extractContent(chunk);
        fullResponse.append(content);
        System.out.print(content);
    }
    
    @Override
    public void onComplete() {
        System.out.println("\n\nStreaming completed!");
        System.out.println("Full response: " + fullResponse.toString());
    }
    
    @Override
    public void onError(Throwable error) {
        System.err.println("Error during streaming: " + error.getMessage());
    }
};

// 使用 FluxStreamHandler 工具类
Flux<ChatModelResponse> flux = chatModel.stream(request);
FluxStreamHandler.subscribe(flux, handler);
```

### Advanced Flux Operations

```java
Flux<ChatModelResponse> flux = chatModel.stream(request);

// 提取并累积文本
flux
    .map(response -> extractText(response))
    .filter(text -> !text.isEmpty())
    .scan("", (accumulated, chunk) -> accumulated + chunk)
    .subscribe(System.out::println);

// 带超时和重试
flux
    .timeout(Duration.ofSeconds(30))
    .retry(3)
    .onErrorResume(error -> {
        log.error("Stream failed, using fallback", error);
        return fallbackModel.stream(request);
    })
    .subscribe(this::processChunk);
```

## Conversation Memory

### Using In-Memory Conversation Memory

```java
import com.llmframework.memory.*;
import com.llmframework.memory.impl.*;

ConversationMemory memory = new InMemoryConversationMemory();
String conversationId = "user-123-session-1";

// Add messages to memory
memory.add(conversationId, createUserMessage("Hello!"));
memory.add(conversationId, createAssistantMessage("Hi! How can I help you today?"));
memory.add(conversationId, createUserMessage("What's the weather like?"));

// Retrieve conversation history
List<Message> history = memory.get(conversationId);

// Continue conversation with context
List<Message> messages = new ArrayList<>(history);
messages.add(createUserMessage("Will I need an umbrella?"));

ChatModelResponse response = chatModel.chat(messages);
memory.add(conversationId, response.getMessage());
```

### Token-Limited Memory

```java
// Get conversation history with token limit
int maxTokens = 4000;
List<Message> recentHistory = memory.get(conversationId, maxTokens);

// This returns the most recent messages that fit within the token limit
```

### Clear Conversation

```java
// Clear a specific conversation
memory.clear(conversationId);
```

## Function Calling

### Defining Functions

```java
import com.llmframework.tool.*;

// Define a function
FunctionDefinition weatherFunction = new FunctionDefinition() {
    @Override
    public String getName() {
        return "get_weather";
    }
    
    @Override
    public String getDescription() {
        return "Get the current weather for a location";
    }
    
    @Override
    public Map<String, Object> getParametersSchema() {
        return Map.of(
            "type", "object",
            "properties", Map.of(
                "location", Map.of(
                    "type", "string",
                    "description", "The city and state, e.g. San Francisco, CA"
                ),
                "unit", Map.of(
                    "type", "string",
                    "enum", List.of("celsius", "fahrenheit")
                )
            ),
            "required", List.of("location")
        );
    }
};
```

### Using Function Calling

```java
// Create a request with function definitions
ChatModelRequest request = ChatModelRequest.builder()
    .messages(List.of(createUserMessage("What's the weather in Boston?")))
    .functions(List.of(weatherFunction))
    .build();

ChatModelResponse response = chatModel.call(request);

// Check if model wants to call a function
Optional<FunctionCall> functionCall = response.getFunctionCall();
if (functionCall.isPresent()) {
    FunctionCall call = functionCall.get();
    System.out.println("Function: " + call.getName());
    System.out.println("Arguments: " + call.getArguments());
    
    // Execute the function
    ToolExecutor executor = createYourToolExecutor();
    ToolExecutionResult result = executor.execute(call);
    
    // Send result back to model
    // ... continue conversation with function result
}
```

### Tool Executor

```java
import com.llmframework.tool.*;

ToolExecutor executor = new SimpleToolExecutor();

// Register a tool
Tool weatherTool = new Tool() {
    @Override
    public Object execute(Map<String, Object> arguments) {
        String location = (String) arguments.get("location");
        String unit = (String) arguments.getOrDefault("unit", "celsius");
        
        // Call your weather API here
        return getWeatherFromAPI(location, unit);
    }
    
    @Override
    public FunctionDefinition getDefinition() {
        return weatherFunction;
    }
};

executor.registerTool("get_weather", weatherTool);

// Execute function calls
ToolExecutionResult result = executor.execute(functionCall);
if (result.isSuccess()) {
    System.out.println("Result: " + result.getResult());
} else {
    System.err.println("Error: " + result.getError());
}
```

## Observability

### Using Logging Observer

```java
import com.llmframework.observability.*;
import com.llmframework.observability.impl.*;

// Create observer
ModelObserver observer = new LoggingObserver();

// Attach to model (implementation specific)
// model.addObserver(observer);

// All calls will now be logged automatically
```

### Cost Calculation

```java
import com.llmframework.observability.*;
import java.math.BigDecimal;

CostCalculator calculator = createYourCostCalculator();

// Calculate cost for a call
ResultMetadata metadata = response.getMetadata();
BigDecimal cost = calculator.calculateCost(metadata, "gpt-4");
System.out.println("Cost: $" + cost);

// Get pricing info
PricingInfo pricing = calculator.getPricingInfo("gpt-4");
System.out.println("Input token price: $" + pricing.getInputTokenPrice());
System.out.println("Output token price: $" + pricing.getOutputTokenPrice());
```

### Composite Observer

```java
import com.llmframework.observability.impl.*;

CompositeObserver composite = new CompositeObserver();
composite.addObserver(new LoggingObserver());
composite.addObserver(new MetricsObserver());
composite.addObserver(new TracingObserver());

// All observers will be notified of events
```

## Retry Strategies

### Exponential Backoff

```java
import com.llmframework.retry.*;
import com.llmframework.retry.impl.*;

// Create exponential backoff strategy
RetryStrategy strategy = new ExponentialBackoffRetryStrategy(
    3,        // max attempts
    1000,     // initial delay (ms)
    2.0,      // multiplier
    60000     // max delay (ms)
);

// Configure model with strategy (implementation specific)
```

### Fixed Delay

```java
// Fixed delay between retries
RetryStrategy strategy = new FixedDelayRetryStrategy(
    5,      // max attempts
    2000    // delay (ms)
);
```

### No Retry

```java
// Disable retries
RetryStrategy strategy = new NoRetryStrategy();
```

### Custom Retry Logic

```java
RetryStrategy customStrategy = new RetryStrategy() {
    @Override
    public boolean shouldRetry(ModelException exception, int attemptNumber) {
        // Only retry rate limit errors, up to 5 times
        return exception instanceof RateLimitException && attemptNumber < 5;
    }
    
    @Override
    public long getRetryDelay(int attemptNumber) {
        // Wait based on rate limit headers if available
        if (exception instanceof RateLimitException rle) {
            return rle.getRetryAfterSeconds() * 1000;
        }
        return 1000;
    }
    
    @Override
    public int getMaxAttempts() {
        return 5;
    }
};
```

## Output Parsing

### JSON Output Parser

```java
import com.llmframework.parser.*;

// Define your data class
class PersonInfo {
    String name;
    int age;
    String occupation;
}

// Create parser
OutputParser<PersonInfo> parser = new JsonOutputParser<>(PersonInfo.class);

// Get format instructions to include in prompt
String formatInstructions = parser.getFormatInstructions();
String prompt = "Extract person information from the following text. " + formatInstructions;

// Parse response
ChatModelResponse response = chatModel.chat(messages);
String output = response.getResult().getOutput().toString();

try {
    PersonInfo person = parser.parse(output);
    System.out.println("Name: " + person.name);
    System.out.println("Age: " + person.age);
} catch (ParseException e) {
    System.err.println("Failed to parse: " + e.getMessage());
}
```

## Advanced Examples

### Complete Application Example

```java
import com.llmframework.chat.*;
import com.llmframework.memory.*;
import com.llmframework.memory.impl.*;
import com.llmframework.observability.*;
import com.llmframework.observability.impl.*;
import com.llmframework.retry.*;
import com.llmframework.retry.impl.*;

public class ChatbotApplication {
    private final ChatModel chatModel;
    private final ConversationMemory memory;
    private final ModelObserver observer;
    
    public ChatbotApplication() {
        // Configure retry strategy
        RetryStrategy retryStrategy = new ExponentialBackoffRetryStrategy();
        
        // Setup observability
        CompositeObserver composite = new CompositeObserver();
        composite.addObserver(new LoggingObserver());
        this.observer = composite;
        
        // Create chat model with configuration
        this.chatModel = createChatModel(retryStrategy, observer);
        
        // Initialize memory
        this.memory = new InMemoryConversationMemory();
    }
    
    public String chat(String userId, String message) {
        String conversationId = "user-" + userId;
        
        // Get conversation history
        List<Message> history = memory.get(conversationId, 4000);
        
        // Add new message
        Message userMessage = createUserMessage(message);
        memory.add(conversationId, userMessage);
        
        // Prepare messages for model
        List<Message> messages = new ArrayList<>(history);
        messages.add(userMessage);
        
        // Call model
        ChatModelResponse response = chatModel.chat(messages);
        
        // Store response
        memory.add(conversationId, response.getMessage());
        
        // Return response text
        return extractText(response);
    }
    
    public void clearConversation(String userId) {
        memory.clear("user-" + userId);
    }
}
```

## Best Practices

1. **Always handle exceptions**: LLM calls can fail for various reasons
2. **Use retry strategies**: Network issues and rate limits are common
3. **Implement observability**: Monitor your LLM usage and costs
4. **Manage conversation context**: Use memory with token limits
5. **Validate inputs**: Check user inputs before sending to models
6. **Stream when possible**: Better user experience for long responses
7. **Use structured outputs**: Parse LLM responses into typed objects
8. **Test with mocks**: Don't rely on real API calls for testing

## Next Steps

- Implement your vendor-specific adapters (OpenAI, Azure, Anthropic, etc.)
- Add custom tools and functions for your use case
- Configure monitoring and alerting
- Optimize prompt templates for your domain
- Implement caching for common queries
- Add rate limiting and circuit breakers
