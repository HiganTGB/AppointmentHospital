package appointmenthospital.appointmentservice.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import java.sql.Timestamp;
@Entity
public class Examination extends BaseEntity {
    private long roomId;
    private String medicalSpeciatly;
    @ManyToOne(targetEntity = PatientProfile.class)
    private PatientProfile patientProfile;
    private long doctorId;
    private long account_id;
    private Long number;
    private Timestamp time;
    private int status;
    private Long price;
}
