package com.abdullah.misar.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CheckInRequest(
        @NotEmpty @Valid List<AnswerRequest> answers
) {}
