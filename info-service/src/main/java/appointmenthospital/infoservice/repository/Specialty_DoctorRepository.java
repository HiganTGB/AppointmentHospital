package appointmenthospital.infoservice.repository;

import appointmenthospital.infoservice.model.entity.Specialty_Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Specialty_DoctorRepository extends JpaRepository<Specialty_Doctor,Long> {

}
