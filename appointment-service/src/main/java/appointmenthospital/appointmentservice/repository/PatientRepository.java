package appointmenthospital.appointmentservice.repository;

import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientProfile,Long> {
    PatientProfile getFirstByPatientUUID(String uuid);
    Boolean existsByPatientUUIDAndAccount_id(String uuid,long account_id);
}
