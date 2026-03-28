package com.abdullah.misar.controller;

import com.abdullah.misar.dto.HistoryPoint;
import com.abdullah.misar.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/history")
    public List<HistoryPoint> getHistory(
            @RequestParam Long questionId,
            @RequestParam(defaultValue = "30") int days) {
        return dashboardService.getHistory(questionId, days);
    }
}
