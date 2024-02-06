package AsimMertTopal.enoca.eCommerceApp.repository;

import AsimMertTopal.enoca.eCommerceApp.entities.Cart;
import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserAndProduct(User user, Product product);

    List<Cart> findByUser(User user);

    List<Cart> findByUser_Id(Long userId);
}
