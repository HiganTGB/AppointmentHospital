package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
public class Examination extends BaseEntity {
    private long roomId;
    private String medicalSpecialty;
    @ManyToOne(targetEntity = PatientProfile.class)
    private PatientProfile patientProfile;
    private long doctorId;
    private long account_id;
    private Long number;
    private Timestamp time;
    private int status;
    private BigDecimal price;
}
