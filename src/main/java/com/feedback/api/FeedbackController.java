package com.feedback.api;

import com.feedback.api.dto.FeedbackDTO;
import com.feedback.domain.Feedback;
import com.feedback.domain.ports.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {
    private final FeedbackService feedbackService;

    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @PostMapping
    public ResponseEntity<FeedbackDTO> createFeedback(@Valid @RequestBody FeedbackDTO feedbackDTO) {
        Feedback feedback = convertToEntity(feedbackDTO);
        Feedback savedFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.ok(convertToDTO(savedFeedback));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> feedbackList = feedbackService.getAllFeedback().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(feedbackList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FeedbackDTO> getFeedbackById(@PathVariable Long id) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(convertToDTO(feedback));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDTO> updateFeedback(@PathVariable Long id, 
                                                    @Valid @RequestBody FeedbackDTO feedbackDTO) {
        // Get existing feedback to preserve creation time
        Feedback existingFeedback = feedbackService.getFeedbackById(id);
        Feedback feedback = new Feedback(
            id,
            feedbackDTO.email(),
            feedbackDTO.content(),
            existingFeedback.getCreatedAt() // Preserve the original creation time
        );
        Feedback updatedFeedback = feedbackService.updateFeedback(id, feedback);
        return ResponseEntity.ok(convertToDTO(updatedFeedback));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }

    private FeedbackDTO convertToDTO(Feedback feedback) {
        return new FeedbackDTO(
                feedback.getId(),
                feedback.getEmail(),
                feedback.getContent(),
                feedback.getCreatedAt()
        );
    }

    private Feedback convertToEntity(FeedbackDTO dto) {
        return new Feedback(
                dto.id(),
                dto.email(),
                dto.content(),
                dto.createdAt()
        );
    }
}
