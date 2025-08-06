package com.example.retrysystem.service;

import com.example.retrysystem.config.RabbitMQConfig;
import com.example.retrysystem.dto.MyPayload;
import com.example.retrysystem.entity.WebhookEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryMessageProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendToQueue(WebhookEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event);
    }
}
