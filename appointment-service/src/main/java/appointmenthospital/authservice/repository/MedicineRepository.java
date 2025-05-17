package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long>, QuerydslPredicateExecutor<Medicine> {
}
