package AsimMertTopal.enoca.eCommerceApp.repository;

import AsimMertTopal.enoca.eCommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
}
