# Changelog

## [1.0.0-SNAPSHOT] - 2024

### Major Changes

#### 🔄 Replaced Java Stream with Reactor Flux for Streaming

**Breaking Change**: All streaming methods now return `Flux<RESP>` instead of `Stream<RESP>`.

**Rationale**: 
- Better support for asynchronous, non-blocking operations
- Built-in backpressure handling
- Richer set of reactive operators
- More suitable for real-time LLM streaming scenarios
- Better integration with reactive frameworks (Spring WebFlux, etc.)

**Affected Interfaces**:
- `Model.stream()`: Now returns `Flux<RESP>`
- `ModelClient.executeStream()`: Now returns `Flux<RESP>`

**Migration Guide**:

Before (Java Stream):
```java
Stream<ChatModelResponse> stream = chatModel.stream(request);
stream.forEach(chunk -> process(chunk));
```

After (Reactor Flux):
```java
Flux<ChatModelResponse> flux = chatModel.stream(request);
flux.subscribe(
    chunk -> process(chunk),
    error -> handleError(error),
    () -> onComplete()
);
```

### Added

- ✨ **Reactor Core 3.6.0** dependency for reactive streaming
- ✨ **Reactor Test 3.6.0** test dependency for testing reactive streams
- ✨ `FluxStreamHandler` utility class for bridging Flux and StreamHandler
- 📚 **REACTIVE_STREAMING.md** - Comprehensive guide for using Reactor Flux
- 📚 **CHANGELOG.md** - This file

### Changed

- 🔄 `Model.stream()` now returns `Flux<RESP>` instead of `Stream<RESP>`
- 🔄 `ModelClient.executeStream()` now returns `Flux<RESP>` instead of `Stream<RESP>`
- 📝 Updated README.md with Reactor Flux examples
- 📝 Updated EXAMPLES.md with Flux-based streaming examples
- 📝 Updated QUICKSTART.md to mention Flux
- 📝 Updated IMPLEMENTATION_NOTES.md with Flux information

### Dependencies

**Added**:
- `io.projectreactor:reactor-core:3.6.0` (compile)
- `io.projectreactor:reactor-test:3.6.0` (test)

**Existing**:
- `org.slf4j:slf4j-api:2.0.9`
- `org.junit.jupiter:junit-jupiter:5.10.1` (test)

## Benefits of Using Reactor Flux

### 1. Non-blocking Asynchronous
```java
// Process multiple streams concurrently without blocking
Flux<ChatModelResponse> flux1 = model1.stream(request1);
Flux<ChatModelResponse> flux2 = model2.stream(request2);
Flux.merge(flux1, flux2).subscribe(this::process);
```

### 2. Built-in Backpressure
```java
// Automatically handles slow consumers
flux.onBackpressureBuffer(100)
    .subscribe(this::slowProcess);
```

### 3. Rich Operators
```java
// Powerful stream transformations
flux
    .map(this::extractText)
    .filter(text -> !text.isEmpty())
    .scan("", (acc, chunk) -> acc + chunk)
    .timeout(Duration.ofSeconds(30))
    .retry(3)
    .subscribe(System.out::println);
```

### 4. Better Error Handling
```java
// Fallback strategies
flux
    .onErrorResume(error -> fallbackModel.stream(request))
    .subscribe(this::process);
```

### 5. Composability
```java
// Easy to combine multiple async operations
Flux.zip(
    model.stream(request),
    embeddingModel.stream(embeddingRequest)
).subscribe(tuple -> processBundle(tuple.getT1(), tuple.getT2()));
```

## Use Cases

### Real-time Chat Applications
Perfect for streaming chat responses in web applications.

### Spring WebFlux Integration
Native integration with Spring WebFlux for reactive REST APIs.

### Server-Sent Events (SSE)
Direct mapping to SSE for browser streaming.

### Parallel Processing
Process multiple model calls concurrently without thread management.

### Complex Workflows
Chain multiple LLM calls with error handling and retries.

## Testing

Use Reactor Test for testing:
```java
@Test
void testStreaming() {
    Flux<ChatModelResponse> flux = chatModel.stream(request);
    
    StepVerifier.create(flux)
        .expectNextCount(5)
        .expectComplete()
        .verify();
}
```

## Resources

- [REACTIVE_STREAMING.md](REACTIVE_STREAMING.md) - Detailed usage guide
- [Project Reactor Documentation](https://projectreactor.io/docs)
- [Reactor Core Reference](https://projectreactor.io/docs/core/release/reference/)
- [Spring WebFlux Integration Examples](EXAMPLES.md#spring-webflux-集成)

## Backward Compatibility

⚠️ **Breaking Change**: Code using `Stream<RESP>` must be updated to use `Flux<RESP>`.

For minimal changes, you can convert Flux to blocking:
```java
// Emergency backward compatibility (not recommended)
List<ChatModelResponse> results = flux.collectList().block();
// Then process as before
```

However, we strongly recommend embracing the reactive paradigm for better performance and scalability.
