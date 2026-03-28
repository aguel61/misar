package com.abdullah.misar.service;

import com.abdullah.misar.dto.AnswerRequest;
import com.abdullah.misar.dto.AnswerResponse;
import com.abdullah.misar.dto.CheckInRequest;
import com.abdullah.misar.dto.CheckInResponse;
import com.abdullah.misar.exception.AlreadyCheckedInException;
import com.abdullah.misar.exception.NotFoundException;
import com.abdullah.misar.exception.ValidationException;
import com.abdullah.misar.factory.QuestionTypeHandler;
import com.abdullah.misar.factory.QuestionTypeHandlerFactory;
import com.abdullah.misar.model.Answer;
import com.abdullah.misar.model.CheckIn;
import com.abdullah.misar.model.Question;
import com.abdullah.misar.repository.AnswerRepository;
import com.abdullah.misar.repository.CheckInRepository;
import com.abdullah.misar.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionTypeHandlerFactory handlerFactory;

    public CheckInResponse findToday() {
        return checkInRepository.findByCheckInDate(LocalDate.now())
                .map(CheckInResponse::from)
                .orElseThrow(() -> new NotFoundException("No check-in found for today"));
    }

    public List<CheckInResponse> findAll() {
        return checkInRepository.findAll().stream()
                .map(CheckInResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<AnswerResponse> findAnswers(Long checkInId) {
        CheckIn checkIn = checkInRepository.findById(checkInId)
                .orElseThrow(() -> new NotFoundException("CheckIn not found with id: " + checkInId));
        return checkIn.getAnswers().stream()
                .map(AnswerResponse::from)
                .toList();
    }

    @Transactional
    public CheckInResponse submit(CheckInRequest request) {
        if (checkInRepository.existsByCheckInDate(LocalDate.now())) {
            throw new AlreadyCheckedInException();
        }

        CheckIn checkIn = CheckIn.builder()
                .checkInDate(LocalDate.now())
                .build();
        checkIn = checkInRepository.save(checkIn);

        for (AnswerRequest answerRequest : request.answers()) {
            Question question = questionRepository.findById(answerRequest.questionId())
                    .orElseThrow(() -> new NotFoundException("Question not found with id: " + answerRequest.questionId()));

            QuestionTypeHandler handler = handlerFactory.getHandler(question.getQuestionType());
            String error = handler.validate(answerRequest.value());
            if (error != null) {
                throw new ValidationException("Invalid value for question '" + question.getLabel() + "': " + error);
            }

            Answer answer = Answer.builder()
                    .checkIn(checkIn)
                    .question(question)
                    .value(handler.normalize(answerRequest.value()))
                    .build();
            answerRepository.save(answer);
        }

        return CheckInResponse.from(checkIn);
    }

    @Transactional
    public CheckInResponse resubmit(CheckInRequest request) {
        CheckIn checkIn = checkInRepository.findByCheckInDate(LocalDate.now())
                .orElseThrow(() -> new NotFoundException("No check-in found for today to update"));

        answerRepository.deleteAllByCheckInId(checkIn.getId());

        for (AnswerRequest answerRequest : request.answers()) {
            Question question = questionRepository.findById(answerRequest.questionId())
                    .orElseThrow(() -> new NotFoundException("Question not found with id: " + answerRequest.questionId()));

            QuestionTypeHandler handler = handlerFactory.getHandler(question.getQuestionType());
            String error = handler.validate(answerRequest.value());
            if (error != null) {
                throw new ValidationException("Invalid value for question '" + question.getLabel() + "': " + error);
            }

            Answer answer = Answer.builder()
                    .checkIn(checkIn)
                    .question(question)
                    .value(handler.normalize(answerRequest.value()))
                    .build();
            answerRepository.save(answer);
        }

        return CheckInResponse.from(checkIn);
    }

    @Transactional
    public void resetToday() {
        CheckIn checkIn = checkInRepository.findByCheckInDate(LocalDate.now())
                .orElseThrow(() -> new NotFoundException("No check-in found for today to reset"));
        checkInRepository.delete(checkIn);
    }
}
