package appointmenthospital.infoservice.repository;

import appointmenthospital.infoservice.model.entity.MedicalSpecialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalSpecialtyRepository extends JpaRepository<MedicalSpecialty,Long>, QuerydslPredicateExecutor<MedicalSpecialty> {

}
