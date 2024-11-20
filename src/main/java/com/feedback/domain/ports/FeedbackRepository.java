package com.feedback.domain.ports;

import com.feedback.domain.Feedback;
import java.util.List;
import java.util.Optional;

public interface FeedbackRepository {
    Feedback save(Feedback feedback);
    List<Feedback> findAll();
    Optional<Feedback> findById(Long id);
    void deleteById(Long id);
    boolean existsById(Long id);
}
