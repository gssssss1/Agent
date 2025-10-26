package com.llmframework.prompt.impl;

import com.llmframework.prompt.Prompt;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashSet;

public class SimplePrompt implements Prompt {
    private final String content;
    private final Set<String> variables;
    
    public SimplePrompt(String content) {
        this.content = content;
        this.variables = extractVariables(content);
    }
    
    @Override
    public String render(Map<String, Object> variables) {
        String result = content;
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            result = result.replace(placeholder, String.valueOf(entry.getValue()));
        }
        return result;
    }
    
    @Override
    public String getContent() {
        return content;
    }
    
    @Override
    public Set<String> getVariables() {
        return variables;
    }
    
    private Set<String> extractVariables(String content) {
        Set<String> vars = new HashSet<>();
        Pattern pattern = Pattern.compile("\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            vars.add(matcher.group(1));
        }
        return vars;
    }
}
