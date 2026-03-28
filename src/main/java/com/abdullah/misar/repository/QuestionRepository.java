package com.abdullah.misar.repository;

import com.abdullah.misar.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByActiveTrueOrderByOrderIndexAsc();
}
