package AsimMertTopal.enoca.eCommerceApp.dto.order;

import AsimMertTopal.enoca.eCommerceApp.dto.cart.ProductCartDto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        Long id,
        Long userId,
        Long productId,
        int quantity,
        double totalAmount,
        String address,
        String phoneNumber,
        String name,
        String surname,
        String city,
        Integer cardNumber,
        Integer cvv,
        List<ProductCartDto> productsInCart,


        LocalDateTime orderDate

) {
}
