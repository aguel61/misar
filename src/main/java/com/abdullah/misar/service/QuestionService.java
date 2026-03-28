package com.abdullah.misar.service;

import com.abdullah.misar.dto.QuestionRequest;
import com.abdullah.misar.dto.QuestionResponse;
import com.abdullah.misar.exception.NotFoundException;
import com.abdullah.misar.exception.ValidationException;
import com.abdullah.misar.model.Question;
import com.abdullah.misar.repository.AnswerRepository;
import com.abdullah.misar.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public List<QuestionResponse> findAll() {
        return questionRepository.findAll().stream()
                .map(QuestionResponse::from)
                .toList();
    }

    public List<QuestionResponse> findActive() {
        return questionRepository.findByActiveTrueOrderByOrderIndexAsc().stream()
                .map(QuestionResponse::from)
                .toList();
    }

    public QuestionResponse create(QuestionRequest request) {
        Question question = Question.builder()
                .label(request.label())
                .questionType(request.questionType())
                .orderIndex(request.orderIndex())
                .active(true)
                .build();
        return QuestionResponse.from(questionRepository.save(question));
    }

    public QuestionResponse update(Long id, QuestionRequest request) {
        Question question = findById(id);
        question.setLabel(request.label());
        question.setQuestionType(request.questionType());
        question.setOrderIndex(request.orderIndex());
        return QuestionResponse.from(questionRepository.save(question));
    }

    public QuestionResponse toggleActive(Long id) {
        Question question = findById(id);
        question.setActive(!question.isActive());
        return QuestionResponse.from(questionRepository.save(question));
    }

    public void delete(Long id) {
        Question question = findById(id);
        if (answerRepository.existsByQuestion(question)) {
            throw new ValidationException("Cannot delete question with existing answers");
        }
        questionRepository.delete(question);
    }

    private Question findById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Question not found with id: " + id));
    }
}
