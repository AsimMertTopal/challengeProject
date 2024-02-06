package AsimMertTopal.enoca.eCommerceApp.dto.product;

public record ProductSaveDto (
        Long id,
    String name,
    String description,
    double price,
    double stock
) {
}
