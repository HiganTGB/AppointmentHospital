package appointmenthospital.appointmentservice.repository;

import appointmenthospital.appointmentservice.model.entity.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<PatientProfile,Long>, QuerydslPredicateExecutor<PatientProfile> {
    PatientProfile getFirstByPatientUUID(String uuid);
}
