package com.llmframework.example;

import com.llmframework.chat.ChatInput;
import com.llmframework.chat.ChatModel;
import com.llmframework.chat.ChatOutput;
import com.llmframework.core.message.Message;
import com.llmframework.core.model.ModelOptions;

import java.util.List;

/**
 * Basic usage example
 * 
 * This demonstrates the core functionality of the framework.
 * Note: This is a conceptual example. Actual implementation requires
 * a concrete ChatModel implementation (e.g., OpenAIChatModel)
 */
public class BasicExample {

    public static void main(String[] args) {
        // Example 1: Create a chat model (pseudo-code)
        // ChatModel chatModel = OpenAIChatModel.builder()
        //     .apiKey(System.getenv("OPENAI_API_KEY"))
        //     .modelName("gpt-4")
        //     .build();

        // Example 2: Simple chat
        // String response = chatModel.chat("What is the capital of France?")
        //     .block();
        // System.out.println(response);

        // Example 3: Multi-turn conversation
        List<Message> messages = List.of(
                Message.system("You are a helpful assistant."),
                Message.user("Tell me about Java 21"),
                Message.assistant("Java 21 is the latest LTS version..."),
                Message.user("What are the main new features?")
        );

        // ChatOutput output = chatModel.call(
        //     ChatInput.builder()
        //         .messages(messages)
        //         .options(opts -> opts
        //             .temperature(0.7)
        //             .maxTokens(500))
        //         .build()
        // ).block();
        //
        // System.out.println(output.content());

        // Example 4: Streaming output
        // chatModel.stream(ChatInput.of("Write a short poem"))
        //     .subscribe(chunk -> System.out.print(chunk.output().content()));

        // Example 5: Structured output
        // record Person(String name, int age, String occupation) {}
        //
        // Person person = chatModel.chatStructured(
        //     "Extract information: John is a 30-year-old software engineer",
        //     Person.class
        // ).block();
        //
        // System.out.println(person);

        System.out.println("Framework structure created successfully!");
        System.out.println("To use this framework:");
        System.out.println("1. Implement a concrete ChatModel (e.g., OpenAIChatModel)");
        System.out.println("2. Configure with your API keys");
        System.out.println("3. Start building LLM applications!");
    }

    /**
     * Example of using custom model options
     */
    private static void customOptionsExample(ChatModel chatModel) {
        ModelOptions creativeOptions = ModelOptions.builder()
                .temperature(0.9)
                .topP(0.95)
                .maxTokens(1000)
                .build();

        ChatInput input = ChatInput.builder()
                .user("Write a creative story")
                .options(creativeOptions)
                .build();

        // chatModel.call(input).subscribe(output ->
        //     System.out.println(output.content())
        // );
    }

    /**
     * Example of conversation with context
     */
    private static void conversationExample(ChatModel chatModel) {
        List<Message> conversation = List.of(
                Message.system("You are a technical expert"),
                Message.user("What is Project Reactor?"),
                Message.assistant("Project Reactor is a reactive programming library..."),
                Message.user("How does it handle backpressure?")
        );

        ChatInput input = ChatInput.builder()
                .messages(conversation)
                .build();

        // chatModel.call(input).subscribe(output ->
        //     System.out.println("Response: " + output.content())
        // );
    }
}
