package appointmenthospital.appointmentservice.repository;

import appointmenthospital.appointmentservice.model.entity.PatientProfile_Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Patient_accountRepository extends JpaRepository<PatientProfile_Account,Long> {
    Boolean existsByAccountIDAndPatientId(Long account_id,Long patient_id);
    PatientProfile_Account getAllByAccountID(Long id);
}
