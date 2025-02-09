package dev.rm.service;

import java.util.Random;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import dev.rm.model.VitalSigns;
import dev.rm.model.VitalSignsMessage;
import dev.rm.repository.VitalSignsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class VitalSignsSimulator {

    private final VitalSignsProducer vitalSignsProducer;

    private static final Random random = new Random();

    private final VitalSignsRepository vitalSignsRepository;

    @Scheduled(fixedRate = 100000) // 10 seconds
    public void generateAndPublishVitalSigns() {
        Long patientId = getRandomPatientId();
        VitalSignsMessage vitalSignsMessage = generateRandomVitalSigns(patientId);
        vitalSignsProducer.sendVitalSigns(vitalSignsMessage);
        log.info("Generated and published new vital signs for patient ID: {}", patientId);
    }

    private VitalSignsMessage generateRandomVitalSigns(Long patientId) {
        VitalSignsMessage vitalSignsMessage = VitalSignsMessage.builder()
                .patientId(patientId)
                .temperature(36.5 + (random.nextDouble() * 2)) // 36.5 to 38.5 C
                .bloodPressure(generateRandomBloodPressure()) // Random BP
                .heartRate(60 + random.nextInt(80)) // 60 to 140 bpm
                .respiratoryRate(12 + random.nextInt(10)) // 12 to 22 breaths/min
                .oxygenSaturation(95 + random.nextInt(5)) // 95% to 100%
                .glucose(70 + random.nextInt(40)) // 70 to 110 mg/dL
                .build();

        VitalSigns vitalSigns = VitalSigns.builder()
                .patientId(patientId)
                .temperature(vitalSignsMessage.getTemperature())
                .bloodPressure(vitalSignsMessage.getBloodPressure())
                .heartRate(vitalSignsMessage.getHeartRate())
                .respiratoryRate(vitalSignsMessage.getRespiratoryRate())
                .oxygenSaturation(vitalSignsMessage.getOxygenSaturation())
                .glucose(vitalSignsMessage.getGlucose())
                .build();

        vitalSignsRepository.save(vitalSigns);

        return vitalSignsMessage;

    }

    private String generateRandomBloodPressure() {
        int systolic = 90 + random.nextInt(40); // 90 to 130 mmHg
        int diastolic = 60 + random.nextInt(30); // 60 to 90 mmHg
        return systolic + "/" + diastolic;
    }

    private Long getRandomPatientId() {
        return (long) (1 + random.nextInt(3));
    }
}
