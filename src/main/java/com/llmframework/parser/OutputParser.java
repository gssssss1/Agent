package com.llmframework.parser;

public interface OutputParser<T> {
    /**
     * 解析输出
     */
    T parse(String output) throws ParseException;
    
    /**
     * 获取格式化指令（用于提示词）
     */
    String getFormatInstructions();
}
