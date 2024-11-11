package appointmenthospital.scheduleservice.repository;

import appointmenthospital.scheduleservice.model.entity.DayOfWeek;
import appointmenthospital.scheduleservice.model.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long>, QuerydslPredicateExecutor<Schedule> {
    Boolean existsByRoomIDAndAtMorningAndDayOfWeek(Long room_id, boolean isDay, DayOfWeek dayOfWeek);
    Boolean existsByDoctorIDAndAtMorningAndDayOfWeek(Long doctor_id, boolean isDay, DayOfWeek dayOfWeek);
    List<Schedule> getAllByRoomID(Long room_id);
    Schedule findFirstByRoomIDAndDayOfWeekAndAtMorning(Long room_id,DayOfWeek dayOfWeek,boolean atMorning);
}
