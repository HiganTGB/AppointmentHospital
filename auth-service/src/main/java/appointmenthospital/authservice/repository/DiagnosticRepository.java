package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Diagnostic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface DiagnosticRepository extends JpaRepository<Diagnostic,Long>, QuerydslPredicateExecutor<Diagnostic> {

}
