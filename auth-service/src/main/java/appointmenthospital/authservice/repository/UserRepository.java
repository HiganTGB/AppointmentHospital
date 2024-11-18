package appointmenthospital.authservice.repository;

import appointmenthospital.authservice.model.entity.PasswordResetToken;
import appointmenthospital.authservice.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {
    Optional<User> findByPhone(String phone );
    Optional<User> findByPasswordResetToken(PasswordResetToken token);
    Page<User> findAllByIsStaffFalse(Pageable pageable);
    Page<User> findAllByIsStaffTrue(Pageable pageable);

    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    boolean existsByPhoneAndPhoneNotLike(String phone,String phoneOld);
    boolean existsByEmailAndEmailNotLike(String email,String emailOld);


}