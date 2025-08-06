package com.example.retrysystem.service;

import com.example.retrysystem.config.RabbitMQConfig;
import com.example.retrysystem.dto.MyPayload;
import com.example.retrysystem.entity.WebhookEvent;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RetryMessageConsumer {
    private final WebhookEventService service;

    public RetryMessageConsumer(WebhookEventService service) {
        this.service = service;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(WebhookEvent event) {
        try {
            service.processEvent(event);
        } catch (Exception e) {
            throw new AmqpRejectAndDontRequeueException("Failed. Don't requeue.");
        }
    }
}
