package AsimMertTopal.enoca.eCommerceApp.service;

import AsimMertTopal.enoca.eCommerceApp.dto.user.UserCreateDto;
import AsimMertTopal.enoca.eCommerceApp.entities.Cart;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import AsimMertTopal.enoca.eCommerceApp.enums.Role;
import AsimMertTopal.enoca.eCommerceApp.repository.CartRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class UserService {
    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    public ResponseEntity<String> addCustomer(UserCreateDto dto) {
        try {
            User user = new User();
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPassword(dto.password());

            user.setRoles(Role.ROLE_CUSTOMER.name());


            Cart cart = new Cart();
            cart.setQuantity(0);
            cart.setProduct(null);
            cart.setUser(user);
            user.setCart(cart);

            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Kullanıcı başarıyla eklendi. Müşteri ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Kullanıcı eklenirken bir hata oluştu" );
        }
    }


    public ResponseEntity<String> addSeller(UserCreateDto dto) {
        try {
            User user = new User();
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPassword(dto.password());

            user.setRoles(Role.ROLE_SELLER.name());

            userRepository.save(user);


            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Kullanıcı başarıyla eklendi. Satıcı ID: " + user.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Kullanıcı eklenirken bir hata oluştu");
        }
    }

}
