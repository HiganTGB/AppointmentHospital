package appointmenthospital.infoservice.repository;

import appointmenthospital.infoservice.model.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room,Long>,QuerydslPredicateExecutor<Room> {
    List<Room> findAllByMedicalSpecialtyId(Long id);
}
