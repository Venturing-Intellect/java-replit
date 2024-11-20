package com.feedback.domain;

import java.time.LocalDateTime;

public class Feedback {
    private Long id;
    private String email;
    private String content;
    private LocalDateTime createdAt;

    public Feedback() {}

    public Feedback(Long id, String email, String content, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.content = content;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
