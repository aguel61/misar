package com.abdullah.misar.dto;

import com.abdullah.misar.model.QuestionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionRequest(
        @NotBlank String label,
        @NotNull QuestionType questionType,
        int orderIndex
) {}
