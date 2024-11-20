package com.feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class FeedbackApplication {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FeedbackApplication.class, args);
    }

    @Component
    public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            logger.info("Application context refreshed, server is ready to accept requests");
            logger.info("Available endpoints:");
            logger.info("POST /api/feedback - Create new feedback");
            logger.info("GET /api/feedback - Get all feedback");
            logger.info("GET /api/feedback/{id} - Get feedback by ID");
            logger.info("PUT /api/feedback/{id} - Update feedback");
            logger.info("DELETE /api/feedback/{id} - Delete feedback");
        }
    }
}
