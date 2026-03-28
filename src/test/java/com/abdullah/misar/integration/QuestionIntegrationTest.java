package com.abdullah.misar.integration;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class QuestionIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void createQuestion_thenGetReturnsIt() throws Exception {
        QuestionRequest request = new QuestionRequest("How did you sleep?", QuestionType.SLIDER, 0);

        mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.label").value("How did you sleep?"))
                .andExpect(jsonPath("$.questionType").value("SLIDER"))
                .andExpect(jsonPath("$.active").value(true));

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void updateQuestion_changesLabel() throws Exception {
        QuestionRequest create = new QuestionRequest("Old label", QuestionType.YES_NO, 0);

        MvcResult createResult = mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        QuestionRequest update = new QuestionRequest("New label", QuestionType.YES_NO, 1);

        mockMvc.perform(put("/api/questions/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.label").value("New label"))
                .andExpect(jsonPath("$.orderIndex").value(1));
    }

    @Test
    void deleteQuestion_withNoAnswers_succeeds() throws Exception {
        QuestionRequest create = new QuestionRequest("Deletable question", QuestionType.TEXT, 0);

        MvcResult createResult = mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(create)))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/api/questions/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/questions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getActiveQuestions_onlyReturnsActiveOnes() throws Exception {
        mockMvc.perform(post("/api/questions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new QuestionRequest("Active Q", QuestionType.SLIDER, 0)))).andReturn();

        MvcResult inactiveResult = mockMvc.perform(post("/api/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QuestionRequest("Inactive Q", QuestionType.TEXT, 1))))
                .andReturn();

        Long inactiveId = objectMapper.readTree(inactiveResult.getResponse().getContentAsString()).get("id").asLong();
        mockMvc.perform(patch("/api/questions/" + inactiveId + "/toggle")).andReturn();

        mockMvc.perform(get("/api/questions/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].label").value("Active Q"));
    }
}
