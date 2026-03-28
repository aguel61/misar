package com.abdullah.misar.repository;

import com.abdullah.misar.model.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByCheckInDate(LocalDate date);
    boolean existsByCheckInDate(LocalDate date);
}
