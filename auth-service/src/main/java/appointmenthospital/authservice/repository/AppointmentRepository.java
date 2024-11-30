package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface AppointmentRepository extends JpaRepository<Appointment,Long>, QuerydslPredicateExecutor<Appointment> {
}
