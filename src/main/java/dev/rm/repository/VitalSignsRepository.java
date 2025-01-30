package dev.rm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.rm.model.VitalSigns;

public interface VitalSignsRepository extends JpaRepository<VitalSigns, Long> {
    List<VitalSigns> findAllByPatientId(Long patientId);
}
