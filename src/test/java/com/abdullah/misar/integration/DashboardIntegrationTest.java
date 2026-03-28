package com.abdullah.misar.integration;

import com.abdullah.misar.model.Answer;
import com.abdullah.misar.model.CheckIn;
import com.abdullah.misar.model.Question;
import com.abdullah.misar.model.QuestionType;
import com.abdullah.misar.repository.AnswerRepository;
import com.abdullah.misar.repository.CheckInRepository;
import com.abdullah.misar.repository.QuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DashboardIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private AnswerRepository answerRepository;

    private MockMvc mockMvc;
    private Long questionId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        Question question = questionRepository.save(Question.builder()
                .label("Sleep quality")
                .questionType(QuestionType.SLIDER)
                .active(true)
                .orderIndex(0)
                .build());
        questionId = question.getId();

        // Seed 5 check-ins on days 5..1 ago (oldest first)
        for (int i = 5; i >= 1; i--) {
            LocalDate date = LocalDate.now().minusDays(i);
            CheckIn checkIn = checkInRepository.save(CheckIn.builder()
                    .checkInDate(date)
                    .build());
            answerRepository.save(Answer.builder()
                    .checkIn(checkIn)
                    .question(question)
                    .value(String.valueOf(i + 4))
                    .build());
        }
    }

    @Test
    void getHistory_returnsCorrectNumberOfPoints() throws Exception {
        mockMvc.perform(get("/api/dashboard/history?questionId=" + questionId + "&days=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));
    }

    @Test
    void getHistory_datesAreInAscendingOrder() throws Exception {
        mockMvc.perform(get("/api/dashboard/history?questionId=" + questionId + "&days=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].date").value(LocalDate.now().minusDays(5).toString()))
                .andExpect(jsonPath("$[4].date").value(LocalDate.now().minusDays(1).toString()));
    }

    @Test
    void getHistory_valuesMatchSeededData() throws Exception {
        mockMvc.perform(get("/api/dashboard/history?questionId=" + questionId + "&days=30"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value("9"))
                .andExpect(jsonPath("$[4].value").value("5"));
    }

    @Test
    void getHistory_withSmallDaysWindow_returnsOnlyRecentPoints() throws Exception {
        mockMvc.perform(get("/api/dashboard/history?questionId=" + questionId + "&days=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getHistory_withUnknownQuestion_returns404() throws Exception {
        mockMvc.perform(get("/api/dashboard/history?questionId=9999&days=30"))
                .andExpect(status().isNotFound());
    }
}
