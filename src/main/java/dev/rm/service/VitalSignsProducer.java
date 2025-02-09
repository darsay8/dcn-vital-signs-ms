package dev.rm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import dev.rm.model.VitalSignsMessage;

@Slf4j
@Service
@RequiredArgsConstructor
public class VitalSignsProducer {

    private final KafkaTemplate<String, VitalSignsMessage> kafkaTemplate;

    private final String topic = "vital_signs";

    public void sendVitalSigns(VitalSignsMessage message) {
        kafkaTemplate.send(topic, message);
        log.info("Sent message to Kafka topic: {}", topic);
    }
}
