package com.abdullah.misar.factory;

import com.abdullah.misar.model.QuestionType;

public interface QuestionTypeHandler {
    QuestionType getType();
    String validate(String rawValue);
    String normalize(String rawValue);
}
