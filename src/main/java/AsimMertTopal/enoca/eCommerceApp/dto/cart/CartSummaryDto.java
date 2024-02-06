package AsimMertTopal.enoca.eCommerceApp.dto.cart;

import java.util.List;

public record CartSummaryDto(List<ProductCartDto> productsInCart, double totalPrice) {
}
