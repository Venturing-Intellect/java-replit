package com.feedback.api;

import com.feedback.api.dto.FeedbackDTO;
import com.feedback.domain.Feedback;
import com.feedback.domain.ports.FeedbackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @Autowired
    private ObjectMapper objectMapper;

    private Feedback testFeedback;
    private FeedbackDTO testFeedbackDTO;

    @BeforeEach
    void setUp() {
        testFeedback = new Feedback(1L, "test@example.com", "Test feedback content", LocalDateTime.now());
        testFeedbackDTO = new FeedbackDTO(1L, "test@example.com", "Test feedback content", LocalDateTime.now());
    }

    @Test
    void createFeedback_ValidInput_ReturnsCreatedFeedback() throws Exception {
        when(feedbackService.createFeedback(any(Feedback.class))).thenReturn(testFeedback);

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testFeedbackDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testFeedbackDTO.email()))
                .andExpect(jsonPath("$.content").value(testFeedbackDTO.content()));
    }

    @Test
    void createFeedback_InvalidEmail_ReturnsBadRequest() throws Exception {
        FeedbackDTO invalidEmailDTO = new FeedbackDTO(1L, "invalid-email", "Test content", LocalDateTime.now());

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }

    @Test
    void createFeedback_EmptyEmail_ReturnsBadRequest() throws Exception {
        FeedbackDTO emptyEmailDTO = new FeedbackDTO(1L, "", "Test content", LocalDateTime.now());

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emptyEmailDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is required"));
    }

    @Test
    void createFeedback_NullEmail_ReturnsBadRequest() throws Exception {
        FeedbackDTO nullEmailDTO = new FeedbackDTO(1L, null, "Test content", LocalDateTime.now());

        mockMvc.perform(post("/api/feedback")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullEmailDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Email is required"));
    }

    @Test
    void getAllFeedback_ReturnsListOfFeedback() throws Exception {
        when(feedbackService.getAllFeedback()).thenReturn(List.of(testFeedback));

        mockMvc.perform(get("/api/feedback"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value(testFeedback.getEmail()))
                .andExpect(jsonPath("$[0].content").value(testFeedback.getContent()));
    }

    @Test
    void updateFeedback_InvalidEmail_ReturnsBadRequest() throws Exception {
        FeedbackDTO invalidEmailDTO = new FeedbackDTO(1L, "invalid@", "Test content", LocalDateTime.now());

        mockMvc.perform(put("/api/feedback/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmailDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }
}
