package com.abdullah.misar.factory;

import com.abdullah.misar.model.QuestionType;
import org.springframework.stereotype.Component;

@Component
public class YesNoHandler implements QuestionTypeHandler {

    @Override
    public QuestionType getType() {
        return QuestionType.YES_NO;
    }

    @Override
    public String validate(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return "Value must not be blank";
        }
        String lower = rawValue.trim().toLowerCase();
        if (!lower.equals("true") && !lower.equals("false")) {
            return "YES_NO value must be 'true' or 'false'";
        }
        return null;
    }

    @Override
    public String normalize(String rawValue) {
        return rawValue.trim().toLowerCase();
    }
}
