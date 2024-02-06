package AsimMertTopal.enoca.eCommerceApp.repository;

import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductSaveDto;
import AsimMertTopal.enoca.eCommerceApp.entities.Cart;
import AsimMertTopal.enoca.eCommerceApp.entities.CartItem;
import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository  extends JpaRepository <CartItem, Long> {
    CartItem findByCartAndProduct(Cart userCart, Product product);

    CartItem findByCartUserAndProduct(User user, Product product);
}
