package com.llmframework.prompt.impl;

import com.llmframework.prompt.Prompt;
import com.llmframework.prompt.PromptTemplate;
import java.util.Map;

public class SimplePromptTemplate implements PromptTemplate {
    private final String template;
    
    public SimplePromptTemplate(String template) {
        this.template = template;
    }
    
    @Override
    public Prompt create(Map<String, Object> variables) {
        SimplePrompt prompt = new SimplePrompt(template);
        return new Prompt() {
            @Override
            public String render(Map<String, Object> vars) {
                return prompt.render(vars);
            }
            
            @Override
            public String getContent() {
                return prompt.render(variables);
            }
            
            @Override
            public java.util.Set<String> getVariables() {
                return prompt.getVariables();
            }
        };
    }
    
    @Override
    public String getTemplate() {
        return template;
    }
    
    @Override
    public boolean validate(Map<String, Object> variables) {
        SimplePrompt prompt = new SimplePrompt(template);
        return variables.keySet().containsAll(prompt.getVariables());
    }
}
