package com.abdullah.misar.repository;

import com.abdullah.misar.model.Answer;
import com.abdullah.misar.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    boolean existsByQuestion(Question question);

    @Modifying
    @Query("DELETE FROM Answer a WHERE a.checkIn.id = :checkInId")
    void deleteAllByCheckInId(@Param("checkInId") Long checkInId);

    @Query("SELECT a FROM Answer a JOIN FETCH a.checkIn c WHERE a.question.id = :questionId AND c.checkInDate >= :from ORDER BY c.checkInDate ASC")
    List<Answer> findHistoryByQuestionIdSince(@Param("questionId") Long questionId, @Param("from") LocalDate from);
}
