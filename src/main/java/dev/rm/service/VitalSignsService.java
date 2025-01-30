package dev.rm.service;

import java.util.List;
import java.util.Optional;

import dev.rm.model.VitalSigns;

public interface VitalSignsService {
    VitalSigns saveVitalSigns(VitalSigns vitalSigns);

    Optional<VitalSigns> getVitalSignsById(Long vitalSignsId);

    List<VitalSigns> getAllVitalSignsByPatientId(Long patientId);

    VitalSigns updateVitalSigns(Long vitalSignsId, VitalSigns vitalSigns);

    void deleteVitalSignsById(Long vitalSignsId);
}
