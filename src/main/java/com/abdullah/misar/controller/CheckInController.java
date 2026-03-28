package com.abdullah.misar.controller;

import com.abdullah.misar.dto.AnswerResponse;
import com.abdullah.misar.dto.CheckInRequest;
import com.abdullah.misar.dto.CheckInResponse;
import com.abdullah.misar.service.CheckInService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkins")
@RequiredArgsConstructor
public class CheckInController {

    private final CheckInService checkInService;

    @GetMapping
    public List<CheckInResponse> findAll() {
        return checkInService.findAll();
    }

    @GetMapping("/today")
    public CheckInResponse findToday() {
        return checkInService.findToday();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CheckInResponse submit(@Valid @RequestBody CheckInRequest request) {
        return checkInService.submit(request);
    }

    @PutMapping("/today")
    public CheckInResponse resubmit(@Valid @RequestBody CheckInRequest request) {
        return checkInService.resubmit(request);
    }

    @DeleteMapping("/today")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetToday() {
        checkInService.resetToday();
    }

    @GetMapping("/{id}/answers")
    public List<AnswerResponse> findAnswers(@PathVariable Long id) {
        return checkInService.findAnswers(id);
    }
}
