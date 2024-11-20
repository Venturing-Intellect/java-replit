package com.feedback.api;

import com.feedback.api.dto.FeedbackDTO;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidationTest {
    private Validator validator;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        now = LocalDateTime.now();
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user@domain.com",
        "user.name@domain.com",
        "user+label@domain.com",
        "user@sub.domain.com",
        "user123@domain.com",
        "user@domain-name.com",
        "12345@domain.com",
        "user@domain.co.uk"
    })
    void testValidEmails(String email) {
        FeedbackDTO feedback = new FeedbackDTO(1L, email, "Test content", now);
        var violations = validator.validate(feedback);
        assertTrue(violations.isEmpty(), "Email " + email + " should be valid");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "@domain.com",
        "user@",
        "user@.",
        "user@domain.",
        "user@.com",
        "user name@domain.com",
        "user@domain..com",
        ".user@domain.com",
        "user.@domain.com",
        "user..name@domain.com"
    })
    void testInvalidEmails(String email) {
        FeedbackDTO feedback = new FeedbackDTO(1L, email, "Test content", now);
        var violations = validator.validate(feedback);
        assertFalse(violations.isEmpty(), "Email " + email + " should be invalid");
        assertTrue(violations.stream()
            .anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }

    @Test
    void testEmptyAndNullEmails() {
        // Test empty email
        FeedbackDTO emptyEmailFeedback = new FeedbackDTO(1L, "", "Test content", now);
        var emptyViolations = validator.validate(emptyEmailFeedback);
        assertFalse(emptyViolations.isEmpty());
        assertTrue(emptyViolations.stream()
            .anyMatch(v -> v.getMessage().equals("Email is required")));

        // Test null email
        FeedbackDTO nullEmailFeedback = new FeedbackDTO(1L, null, "Test content", now);
        var nullViolations = validator.validate(nullEmailFeedback);
        assertFalse(nullViolations.isEmpty());
        assertTrue(nullViolations.stream()
            .anyMatch(v -> v.getMessage().equals("Email is required")));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user!#$%&'*+-/=?^_`{|}~@domain.com",
        "very.unusual.123@example.com",
        "!#$%&'*+-/=?^_`{|}~@domain.example.com",
        "user.name+tag+sorting@example.com",
        "example-indeed@strange-example.com",
        "admin@mailserver1.example.com"
    })
    void testSpecialCharacterEmails(String email) {
        FeedbackDTO feedback = new FeedbackDTO(1L, email, "Test content", now);
        var violations = validator.validate(feedback);
        assertTrue(violations.isEmpty(), "Email " + email + " with special characters should be valid");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "user@domain.cn",
        "user@domain.ru",
        "user@domain.in",
        "user@domain.fr",
        "user@domain.de",
        "user@domain.co.jp",
        "user@domain.com.br",
        "user@domain.com.au"
    })
    void testUnicodeEmails(String email) {
        FeedbackDTO feedback = new FeedbackDTO(1L, email, "Test content", now);
        var violations = validator.validate(feedback);
        assertTrue(violations.isEmpty(), "International domain email " + email + " should be valid");
    }
}
