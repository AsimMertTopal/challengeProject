package AsimMertTopal.enoca.eCommerceApp.dto.cart;

public record AddToCartDto(
                              Long id,
                              String name,
                              String description,
                              double price,
                              double stock) {
}
