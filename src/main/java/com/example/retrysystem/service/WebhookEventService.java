package com.example.retrysystem.service;

import com.example.retrysystem.entity.WebhookEvent;
import com.example.retrysystem.repository.WebhookEventRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Service
public class WebhookEventService {

    private final WebhookEventRepository repository;

    public WebhookEventService(WebhookEventRepository repository) {
        this.repository = repository;
    }

    @Retryable(
        value = { SQLException.class, DataAccessException.class },
        maxAttempts = 3,
        backoff = @Backoff(delay = 5000)
    )
    @Transactional
    public void processEvent(WebhookEvent event) {
        if (repository.findByEventId(event.getEventId()).isPresent()) {
            throw new IllegalStateException("Event already processed");
        }

        event.setStatus("PROCESSING");
        event.setUpdatedAt(LocalDateTime.now());

        repository.save(event);

        event.setStatus("COMPLETED");
        event.setUpdatedAt(LocalDateTime.now());
        repository.save(event);
    }

    @Recover
    public void recover(SQLException e, WebhookEvent event) {
        event.setStatus("FAILED");
        event.setUpdatedAt(LocalDateTime.now());
        repository.save(event);
        System.err.println("Event could not be processed, added as FAILED to logs: " + event.getEventId());
    }
}
