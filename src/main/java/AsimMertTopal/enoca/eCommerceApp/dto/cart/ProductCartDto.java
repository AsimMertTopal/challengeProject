package AsimMertTopal.enoca.eCommerceApp.dto.cart;

public record ProductCartDto( Long id,
                              String name,
                              String description,
                              double price,
                              double stock,
                              int quantity) {
}
