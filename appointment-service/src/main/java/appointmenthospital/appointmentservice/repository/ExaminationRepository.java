package appointmenthospital.appointmentservice.repository;

import appointmenthospital.appointmentservice.model.entity.AppointmentStatus;
import appointmenthospital.appointmentservice.model.entity.Examination;
import appointmenthospital.appointmentservice.model.entity.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface ExaminationRepository extends JpaRepository<Examination,Long> {
    int countAllByDateAndRoomIDAndTimeSlotAndStatusNotLike(LocalDate date, Long room_id, TimeSlot timeSlot, AppointmentStatus appointmentStatus);
}
