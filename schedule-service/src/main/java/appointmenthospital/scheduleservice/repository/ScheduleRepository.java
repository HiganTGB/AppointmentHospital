package appointmenthospital.scheduleservice.repository;

import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long>, QuerydslPredicateExecutor<Schedule> {
    Boolean existsByRoomIDAndIsDayAndDayOfWeek(Long room_id, boolean isDay, DayOfWeek dayOfWeek);
    Boolean existsByDoctorIDAndIsDayAndDayOfWeek(Long doctor_id, boolean isDay, DayOfWeek dayOfWeek);
}
