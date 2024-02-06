package AsimMertTopal.enoca.eCommerceApp.repository;

import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository  extends JpaRepository<Product, Long> {
}
