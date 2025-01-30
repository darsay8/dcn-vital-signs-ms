package dev.rm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import dev.rm.model.VitalSignsMessage;

@Service
@RequiredArgsConstructor
public class VitalSignsProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendVitalSigns(VitalSignsMessage message) {
        rabbitTemplate.convertAndSend("alertQueue", message);
    }
}