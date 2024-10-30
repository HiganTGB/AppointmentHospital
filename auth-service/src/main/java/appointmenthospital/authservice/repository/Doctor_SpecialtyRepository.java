package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Doctor_Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Doctor_SpecialtyRepository extends JpaRepository<Doctor_Specialty,Long> {
}
