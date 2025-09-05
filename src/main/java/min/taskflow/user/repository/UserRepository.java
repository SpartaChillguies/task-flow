package min.taskflow.user.repository;

import min.taskflow.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String userName);  //중복 검증을 위함

    boolean existsByEmail(String email); //중복 검증을 위함

    //Optional<User> findByEmail(String mail);

    Optional<User> findByUserName(String userName);

}
