package appointmenthospital.authservice.repository;


import appointmenthospital.authservice.model.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface DoctorRepository extends JpaRepository<Doctor,Long>, QuerydslPredicateExecutor<Doctor> {

}
