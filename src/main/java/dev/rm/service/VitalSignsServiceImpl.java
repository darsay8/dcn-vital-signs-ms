package dev.rm.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dev.rm.model.VitalSigns;
import dev.rm.model.VitalSignsMessage;
import dev.rm.repository.VitalSignsRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class VitalSignsServiceImpl implements VitalSignsService {

    private final VitalSignsRepository vitalSignsRepository;
    private final VitalSignsProducer vitalSignsProducer;

    @Override
    public VitalSigns saveVitalSigns(VitalSigns vitalSigns) {
        VitalSigns savedVitalSigns = vitalSignsRepository.save(vitalSigns);

        VitalSignsMessage message = new VitalSignsMessage(
                savedVitalSigns.getPatientId(),
                savedVitalSigns.getTemperature(),
                savedVitalSigns.getBloodPressure(),
                savedVitalSigns.getHeartRate(),
                savedVitalSigns.getRespiratoryRate(),
                savedVitalSigns.getOxygenSaturation(),
                savedVitalSigns.getGlucose());

        vitalSignsProducer.sendVitalSigns(message);
        return savedVitalSigns;
    }

    @Override
    public Optional<VitalSigns> getVitalSignsById(Long id) {
        return vitalSignsRepository.findById(id);
    }

    @Override
    public List<VitalSigns> getAllVitalSignsByPatientId(Long patientId) {
        return vitalSignsRepository.findAllByPatientId(patientId);
    }

    @Override
    public VitalSigns updateVitalSigns(Long id, VitalSigns updatedVitalSigns) {
        return vitalSignsRepository.findById(id)
                .map(vitalSign -> {
                    log.info("Updating vital signs ID: {}", id);
                    vitalSign.setTemperature(updatedVitalSigns.getTemperature());
                    vitalSign.setBloodPressure(updatedVitalSigns.getBloodPressure());
                    vitalSign.setHeartRate(updatedVitalSigns.getHeartRate());
                    vitalSign.setRespiratoryRate(updatedVitalSigns.getRespiratoryRate());
                    vitalSign.setOxygenSaturation(updatedVitalSigns.getOxygenSaturation());
                    vitalSign.setGlucose(updatedVitalSigns.getGlucose());
                    vitalSign.setNotes(updatedVitalSigns.getNotes());
                    vitalSign.setTimestamp(LocalDateTime.now());
                    return vitalSignsRepository.save(vitalSign);
                })
                .orElseThrow(() -> {
                    log.error("Vital Signs not found with ID: {}", id);
                    return new RuntimeException("Vital Signs not found with ID: " + id);
                });
    }

    @Override
    public void deleteVitalSignsById(Long id) {
        if (!vitalSignsRepository.existsById(id)) {
            log.warn("Attempted to delete non-existent vital signs ID: {}", id);
            throw new RuntimeException("Vital Signs not found with ID: " + id);
        }
        log.info("Deleting vital signs ID: {}", id);
        vitalSignsRepository.deleteById(id);
    }
}
