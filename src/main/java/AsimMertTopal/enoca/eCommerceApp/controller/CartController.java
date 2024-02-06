package AsimMertTopal.enoca.eCommerceApp.controller;

import AsimMertTopal.enoca.eCommerceApp.dto.cart.CartSummaryDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductSaveDto;
import AsimMertTopal.enoca.eCommerceApp.service.CartService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Data
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart/{productId}/{userId}")
    public ResponseEntity<?> addProductToCart(
            @PathVariable Long productId,
            @PathVariable Long userId
    ) {
        return cartService.addProductToCart(productId, userId);
    }

    @GetMapping("/getCart/{userId}")
    public CartSummaryDto getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PutMapping("/updateCart/{cartId}")
    public ResponseEntity<ProductSaveDto> updateCart(
            @PathVariable Long cartId,
            @PathVariable int newQuantity
    ) {
        return cartService.updateCart(cartId, newQuantity);
    }

    @PutMapping("/decreaseQuantity/{cartId}/{productId}")
    public ResponseEntity<String> decreaseCartItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long productId
    ) {
        return cartService.decreaseCartItemQuantity(cartId, productId);
    }


    @DeleteMapping("/removeProduct/{userId}/{productId}")
    public ResponseEntity<?> removeProductFromUserCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        return cartService.removeProductFromUserCart(userId, productId);
    }

    @DeleteMapping("/emptyCart/{userId}")
    public ResponseEntity<Void> emptyCart(
            @PathVariable Long userId
    ) {
        return cartService.emptyCart(userId);
    }
}
