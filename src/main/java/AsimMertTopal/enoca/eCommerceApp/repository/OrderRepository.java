package AsimMertTopal.enoca.eCommerceApp.repository;

import AsimMertTopal.enoca.eCommerceApp.entities.Order;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);

    Optional<Order> findByIdAndUser(Long orderId, User user);
}
