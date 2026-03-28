package com.abdullah.misar.dto;

import com.abdullah.misar.model.CheckIn;

import java.time.LocalDate;

public record CheckInResponse(
        Long id,
        LocalDate checkInDate
) {
    public static CheckInResponse from(CheckIn c) {
        return new CheckInResponse(c.getId(), c.getCheckInDate());
    }
}
