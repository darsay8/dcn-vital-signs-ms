package dev.rm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.rm.model.VitalSigns;

public interface Repository extends JpaRepository<VitalSigns, Long> {

}
