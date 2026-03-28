package com.abdullah.misar.integration;

import com.abdullah.misar.dto.AnswerRequest;
import com.abdullah.misar.dto.CheckInRequest;
import com.abdullah.misar.dto.QuestionRequest;
import com.abdullah.misar.model.QuestionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CheckInIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;
    private Long sliderQuestionId;

    @BeforeEach
    void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        MvcResult result = mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new QuestionRequest("How did you sleep?", QuestionType.SLIDER, 0))))
                .andReturn();
        sliderQuestionId = objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    @Test
    void submitCheckIn_returnsCreated() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "7")));

        mockMvc.perform(post("/api/checkins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.checkInDate").isNotEmpty());
    }

    @Test
    void submitCheckIn_twice_returns409() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "5")));

        mockMvc.perform(post("/api/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(post("/api/checkins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void getTodayCheckIn_afterSubmit_returnsIt() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "8")));
        mockMvc.perform(post("/api/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        mockMvc.perform(get("/api/checkins/today"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInDate").isNotEmpty());
    }

    @Test
    void getTodayCheckIn_beforeSubmit_returns404() throws Exception {
        mockMvc.perform(get("/api/checkins/today"))
                .andExpect(status().isNotFound());
    }

    @Test
    void submitCheckIn_withInvalidSliderValue_returns400() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "99")));

        mockMvc.perform(post("/api/checkins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void submitCheckIn_withNonNumericSliderValue_returns400() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "abc")));

        mockMvc.perform(post("/api/checkins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resubmitCheckIn_updatesAnswer() throws Exception {
        // First submit
        mockMvc.perform(post("/api/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "6"))))));

        // Re-submit with updated value
        CheckInRequest update = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "9")));
        mockMvc.perform(put("/api/checkins/today")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.checkInDate").isNotEmpty());

        // Verify the answer was updated
        MvcResult todayResult = mockMvc.perform(get("/api/checkins/today")).andReturn();
        Long checkInId = objectMapper.readTree(todayResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(get("/api/checkins/" + checkInId + "/answers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].value").value("9"));
    }

    @Test
    void resubmitCheckIn_withNoCheckIn_returns404() throws Exception {
        CheckInRequest request = new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "5")));

        mockMvc.perform(put("/api/checkins/today")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void resetTodayCheckIn_deletesCheckIn() throws Exception {
        // Submit first
        mockMvc.perform(post("/api/checkins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(
                        new CheckInRequest(List.of(new AnswerRequest(sliderQuestionId, "7"))))));

        // Reset
        mockMvc.perform(delete("/api/checkins/today"))
                .andExpect(status().isNoContent());

        // Check-in should no longer exist
        mockMvc.perform(get("/api/checkins/today"))
                .andExpect(status().isNotFound());
    }

    @Test
    void resetTodayCheckIn_withNoCheckIn_returns404() throws Exception {
        mockMvc.perform(delete("/api/checkins/today"))
                .andExpect(status().isNotFound());
    }
}
