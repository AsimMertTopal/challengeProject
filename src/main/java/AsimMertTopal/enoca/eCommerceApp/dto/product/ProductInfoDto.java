package AsimMertTopal.enoca.eCommerceApp.dto.product;

public record ProductInfoDto( Long id,
                              String name,
                              String description,
                              double price,
                              double stock,
                              double orderPrice) {
}
