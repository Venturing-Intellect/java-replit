package com.feedback.infrastructure.persistence;

import com.feedback.domain.Feedback;
import com.feedback.domain.ports.FeedbackRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostgresFeedbackRepository implements FeedbackRepository {
    private final JpaFeedbackRepository jpaRepository;

    public PostgresFeedbackRepository(JpaFeedbackRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Feedback save(Feedback feedback) {
        FeedbackJpaEntity entity = toEntity(feedback);
        FeedbackJpaEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public List<Feedback> findAll() {
        return jpaRepository.findAll().stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Feedback> findById(Long id) {
        return jpaRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    private FeedbackJpaEntity toEntity(Feedback feedback) {
        FeedbackJpaEntity entity = new FeedbackJpaEntity();
        entity.setId(feedback.getId());
        entity.setEmail(feedback.getEmail());
        entity.setContent(feedback.getContent());
        entity.setCreatedAt(feedback.getCreatedAt());
        return entity;
    }

    private Feedback toDomain(FeedbackJpaEntity entity) {
        return new Feedback(
                entity.getId(),
                entity.getEmail(),
                entity.getContent(),
                entity.getCreatedAt()
        );
    }
}
