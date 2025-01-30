package dev.rm.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vital_signs")
public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long vitalSignsId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "blood_pressure", nullable = false)
    private String bloodPressure;

    @Column(name = "heart_rate", nullable = false)
    private Integer heartRate;

    @Column(name = "respiratory_rate", nullable = false)
    private Integer respiratoryRate;

    @Column(name = "oxygen_saturation", nullable = false)
    private Integer oxygenSaturation;

    @Column(name = "glucose", nullable = false)
    private Integer glucose;

    @Column(name = "notes")
    private String notes;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @PrePersist
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();
    }
}