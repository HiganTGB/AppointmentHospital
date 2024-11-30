package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PatientRepository extends JpaRepository<Patient,Long>, QuerydslPredicateExecutor<Patient> {
    Patient findByUserId(long id);
}
