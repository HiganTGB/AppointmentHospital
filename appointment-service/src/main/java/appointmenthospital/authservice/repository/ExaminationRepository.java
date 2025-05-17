package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Examination;
import appointmenthospital.authservice.model.entity.ExaminationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ExaminationRepository extends JpaRepository<Examination,Long>, QuerydslPredicateExecutor<Examination> {

}
