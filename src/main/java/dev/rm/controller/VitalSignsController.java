package dev.rm.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.rm.model.VitalSigns;
import dev.rm.service.VitalSignsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@Slf4j
@RestController
@RequestMapping("/api/vital-signs")
@RequiredArgsConstructor
public class VitalSignsController {

    private final VitalSignsService vitalSignsService;

    @PostMapping
    public ResponseEntity<VitalSigns> createVitalSigns(@RequestBody VitalSigns vitalSigns) {
        log.info("Creating new vital signs for patient ID: {}", vitalSigns.getPatientId());
        return ResponseEntity.status(HttpStatus.CREATED).body(vitalSignsService.saveVitalSigns(vitalSigns));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VitalSigns> getVitalSignById(@PathVariable Long id) {
        log.info("Fetching vital signs with ID: {}", id);
        return vitalSignsService.getVitalSignsById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Vital signs not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VitalSigns>> getAllVitalSignsByPatient(@PathVariable Long patientId) {
        log.info("Fetching all vital signs for patient ID: {}", patientId);
        List<VitalSigns> vitalSignsList = vitalSignsService.getAllVitalSignsByPatientId(patientId);
        return vitalSignsList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(vitalSignsList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VitalSigns> updateVitalSigns(@PathVariable Long id,
            @RequestBody VitalSigns updatedVitalSigns) {
        log.info("Updating vital signs with ID: {}", id);
        try {
            return ResponseEntity.ok(vitalSignsService.updateVitalSigns(id, updatedVitalSigns));
        } catch (RuntimeException e) {
            log.error("Error updating vital signs: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVitalSigns(@PathVariable Long id) {
        log.info("Deleting vital signs with ID: {}", id);
        try {
            vitalSignsService.deleteVitalSignsById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            log.error("Error deleting vital signs: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}