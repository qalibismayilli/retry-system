package com.example.retrysystem.api;

import com.example.retrysystem.dto.MyPayload;
import com.example.retrysystem.entity.WebhookEvent;
import com.example.retrysystem.service.RetryMessageProducer;
import com.example.retrysystem.service.WebhookEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookEventService service;
    private final RetryMessageProducer retryMessageProducer;

    public WebhookController(WebhookEventService service, RetryMessageProducer retryMessageProducer) {
        this.service = service;
        this.retryMessageProducer = retryMessageProducer;
    }

    @PostMapping("/payment")
    public ResponseEntity<String> receivePaymentWebhook(@RequestBody Map<String, Object> payload) {
        String eventId = (String) payload.get("eventId");
        String eventType = (String) payload.get("eventType");

        WebhookEvent event = new WebhookEvent();
        event.setEventType(eventType);
        event.setEventId(eventId);
        event.setPayload(payload.toString());

        retryMessageProducer.sendToQueue(event);
        return ResponseEntity.ok("Event queued");
    }
}
