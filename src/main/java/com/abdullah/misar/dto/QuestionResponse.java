package com.abdullah.misar.dto;

import com.abdullah.misar.model.Question;
import com.abdullah.misar.model.QuestionType;

public record QuestionResponse(
        Long id,
        String label,
        QuestionType questionType,
        boolean active,
        int orderIndex
) {
    public static QuestionResponse from(Question q) {
        return new QuestionResponse(q.getId(), q.getLabel(), q.getQuestionType(), q.isActive(), q.getOrderIndex());
    }
}
