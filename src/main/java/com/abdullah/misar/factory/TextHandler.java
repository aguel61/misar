package com.abdullah.misar.factory;

import com.abdullah.misar.model.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class TextHandler implements QuestionTypeHandler {

    private static final int MAX_LENGTH = 500;

    @Override
    public QuestionType getType() {
        return QuestionType.TEXT;
    }

    @Override
    public String validate(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return "Value must not be blank";
        }
        if (rawValue.trim().length() > MAX_LENGTH) {
            return "Text value must not exceed " + MAX_LENGTH + " characters";
        }
        return null;
    }

    @Override
    public String normalize(String rawValue) {
        return rawValue.trim();
    }
}
