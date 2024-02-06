package AsimMertTopal.enoca.eCommerceApp.dto.product;

public record ProductDto (
        Long id,
        String name,
        String description,
        double price,
        double stock
) {
}
