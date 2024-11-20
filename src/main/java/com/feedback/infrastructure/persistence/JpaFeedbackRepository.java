package com.feedback.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFeedbackRepository extends JpaRepository<FeedbackJpaEntity, Long> {
}
