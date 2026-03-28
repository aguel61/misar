package com.abdullah.misar.dto;

import com.abdullah.misar.model.Answer;

public record AnswerResponse(
        Long questionId,
        String label,
        String value
) {
    public static AnswerResponse from(Answer a) {
        return new AnswerResponse(a.getQuestion().getId(), a.getQuestion().getLabel(), a.getValue());
    }
}
