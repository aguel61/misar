package com.abdullah.misar.dto;

import java.time.LocalDate;

public record HistoryPoint(
        LocalDate date,
        String value
) {}
