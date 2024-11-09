package appointmenthospital.infoservice.repository;

import appointmenthospital.infoservice.model.entity.Specialty_Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Specialty_DoctorRepository extends JpaRepository<Specialty_Doctor,Long> {
    List<Specialty_Doctor> getAllByDoctorID(long id);
}
