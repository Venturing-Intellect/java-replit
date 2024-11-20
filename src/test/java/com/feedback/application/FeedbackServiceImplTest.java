package com.feedback.application;

import com.feedback.domain.Feedback;
import com.feedback.domain.ports.FeedbackRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceImplTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackServiceImpl feedbackService;

    private Feedback testFeedback;

    @BeforeEach
    void setUp() {
        testFeedback = new Feedback(1L, "test@example.com", "Test feedback", LocalDateTime.now());
    }

    @Test
    void createFeedback_ValidFeedback_ReturnsSavedFeedback() {
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(testFeedback);

        Feedback result = feedbackService.createFeedback(testFeedback);

        assertNotNull(result);
        assertEquals(testFeedback.getEmail(), result.getEmail());
        assertEquals(testFeedback.getContent(), result.getContent());
        verify(feedbackRepository).save(any(Feedback.class));
    }

    @Test
    void getFeedbackById_ExistingId_ReturnsFeedback() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.of(testFeedback));

        Feedback result = feedbackService.getFeedbackById(1L);

        assertNotNull(result);
        assertEquals(testFeedback.getId(), result.getId());
        verify(feedbackRepository).findById(1L);
    }

    @Test
    void getFeedbackById_NonExistingId_ThrowsException() {
        when(feedbackRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> feedbackService.getFeedbackById(1L));
        verify(feedbackRepository).findById(1L);
    }
}
