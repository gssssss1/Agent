package com.llmframework.chat;

import com.llmframework.core.model.ModelRequest;
import com.llmframework.prompt.Message;
import com.llmframework.tool.FunctionDefinition;
import com.llmframework.tool.ToolDefinition;
import java.util.List;

public interface ChatModelRequest extends ModelRequest {
    /**
     * 获取消息列表
     */
    List<Message> getMessages();
    
    /**
     * 获取函数定义（用于 Function Calling）
     */
    List<FunctionDefinition> getFunctions();
    
    /**
     * 获取工具定义
     */
    List<ToolDefinition> getTools();
    
    static Builder builder() {
        return new Builder();
    }
    
    class Builder {
        private List<Message> messages;
        
        public Builder messages(List<Message> messages) {
            this.messages = messages;
            return this;
        }
        
        public ChatModelRequest build() {
            return new ChatModelRequest() {
                @Override
                public List<Message> getMessages() {
                    return messages;
                }
                
                @Override
                public List<FunctionDefinition> getFunctions() {
                    return null;
                }
                
                @Override
                public List<ToolDefinition> getTools() {
                    return null;
                }
                
                @Override
                public String getInstructions() {
                    return null;
                }
                
                @Override
                public com.llmframework.core.model.ModelOptions getOptions() {
                    return null;
                }
                
                @Override
                public String getRequestId() {
                    return null;
                }
            };
        }
    }
}
