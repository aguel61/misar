package com.abdullah.misar.controller;

import com.abdullah.misar.dto.QuestionRequest;
import com.abdullah.misar.dto.QuestionResponse;
import com.abdullah.misar.service.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<QuestionResponse> findAll() {
        return questionService.findAll();
    }

    @GetMapping("/active")
    public List<QuestionResponse> findActive() {
        return questionService.findActive();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public QuestionResponse create(@Valid @RequestBody QuestionRequest request) {
        return questionService.create(request);
    }

    @PutMapping("/{id}")
    public QuestionResponse update(@PathVariable Long id, @Valid @RequestBody QuestionRequest request) {
        return questionService.update(id, request);
    }

    @PatchMapping("/{id}/toggle")
    public QuestionResponse toggleActive(@PathVariable Long id) {
        return questionService.toggleActive(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        questionService.delete(id);
    }
}
