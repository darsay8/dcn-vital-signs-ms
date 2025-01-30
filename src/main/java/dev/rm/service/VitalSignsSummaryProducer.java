package dev.rm.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import dev.rm.model.VitalSigns;
import dev.rm.repository.VitalSignsRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class VitalSignsSummaryProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private VitalSignsRepository vitalSignsRepository;

    private final ObjectMapper objectMapper;

    // Constructor
    public VitalSignsSummaryProducer(RabbitTemplate rabbitTemplate, VitalSignsRepository vitalSignsRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.vitalSignsRepository = vitalSignsRepository;

        // Initialize ObjectMapper and register JavaTimeModule to handle LocalDateTime
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule()); // Register JavaTimeModule
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Optional: Disable timestamp
                                                                                   // serialization
    }

    @Scheduled(fixedRate = 300000) // 5 minutes
    public void sendSummaryReport() {
        List<VitalSigns> recentVitalSigns = getRecentVitalSigns();

        log.info("Sending summary report request...");
        log.info("Recent vital signs: {}", recentVitalSigns);

        if (!recentVitalSigns.isEmpty()) {
            try {
                String summaryReport = objectMapper.writeValueAsString(recentVitalSigns);

                rabbitTemplate.convertAndSend("summaryQueue", summaryReport);
                log.info("Sent Summary Report: {}", summaryReport);
            } catch (Exception e) {
                log.error("Error sending summary report", e);
            }
        }
    }

    private List<VitalSigns> getRecentVitalSigns() {
        long currentTime = System.currentTimeMillis();
        long fiveMinutesAgo = currentTime - (5 * 60 * 1000); // 5 minutes ago in milliseconds
        return vitalSignsRepository.findByTimestampAfter(java.time.Instant.ofEpochMilli(fiveMinutesAgo)
                .atZone(java.time.ZoneId.systemDefault()).toLocalDateTime());
    }
}
