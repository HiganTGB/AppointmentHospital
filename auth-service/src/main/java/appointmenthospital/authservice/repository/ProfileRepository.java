package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Long>, QuerydslPredicateExecutor<Profile> {

}
