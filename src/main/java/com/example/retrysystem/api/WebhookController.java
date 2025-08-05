package com.example.retrysystem.api;

import com.example.retrysystem.entity.WebhookEvent;
import com.example.retrysystem.service.WebhookEventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookEventService service;

    public WebhookController(WebhookEventService service) {
        this.service = service;
    }

    @PostMapping("/payment")
    public ResponseEntity<String> receivePaymentWebhook(@RequestBody Map<String, Object> payload) {
        String eventId = (String) payload.get("eventId");
        String eventType = (String) payload.get("eventType");

        WebhookEvent event = new WebhookEvent();
        event.setEventId(eventId);
        event.setEventType(eventType);
        event.setPayload(payload.toString());

        try {
            service.processEvent(event);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Event already processed");
        }

        return ResponseEntity.ok("Event received");
    }
}
