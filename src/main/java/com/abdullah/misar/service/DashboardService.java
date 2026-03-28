package com.abdullah.misar.service;

import com.abdullah.misar.dto.HistoryPoint;
import com.abdullah.misar.exception.NotFoundException;
import com.abdullah.misar.model.Answer;
import com.abdullah.misar.repository.AnswerRepository;
import com.abdullah.misar.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public List<HistoryPoint> getHistory(Long questionId, int days) {
        questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundException("Question not found with id: " + questionId));

        LocalDate from = LocalDate.now().minusDays(days);
        List<Answer> answers = answerRepository.findHistoryByQuestionIdSince(questionId, from);

        return answers.stream()
                .map(a -> new HistoryPoint(a.getCheckIn().getCheckInDate(), a.getValue()))
                .toList();
    }
}
