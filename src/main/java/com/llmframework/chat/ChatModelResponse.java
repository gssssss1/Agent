package com.llmframework.chat;

import com.llmframework.core.model.ModelResponse;
import com.llmframework.core.result.TextResult;
import com.llmframework.prompt.Message;
import com.llmframework.tool.FunctionCall;
import com.llmframework.tool.ToolCall;
import java.util.List;
import java.util.Optional;

public interface ChatModelResponse extends ModelResponse<TextResult> {
    /**
     * 获取生成的消息
     */
    Message getMessage();
    
    /**
     * 获取函数调用（如果有）
     */
    Optional<FunctionCall> getFunctionCall();
    
    /**
     * 获取工具调用（如果有）
     */
    List<ToolCall> getToolCalls();
}
