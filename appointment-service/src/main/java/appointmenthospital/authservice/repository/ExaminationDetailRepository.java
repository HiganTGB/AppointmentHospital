package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.ExaminationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ExaminationDetailRepository extends JpaRepository<ExaminationDetail,Long>, QuerydslPredicateExecutor<ExaminationDetail> {
    Optional<ExaminationDetail> findByDiagnosticIdAndExaminationId(long diag_id,long exam_id);
}
