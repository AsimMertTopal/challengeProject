package AsimMertTopal.enoca.eCommerceApp.dto.order;

import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductInfoDto;

import java.time.LocalDateTime;
import java.util.List;

public record UsersOrderDto(
        Long id,
        Long userId,
        Long productId,
        double totalAmount,
        String address,
        String phoneNumber,
        String name,
        String surname,
        String city,
        LocalDateTime orderDate,
        List<ProductInfoDto> productsInOrderDto
) {
}
