package AsimMertTopal.enoca.eCommerceApp.service;

import AsimMertTopal.enoca.eCommerceApp.dto.cart.CartSummaryDto;
import AsimMertTopal.enoca.eCommerceApp.dto.cart.ProductCartDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductSaveDto;
import AsimMertTopal.enoca.eCommerceApp.entities.Cart;
import AsimMertTopal.enoca.eCommerceApp.entities.CartItem;
import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import AsimMertTopal.enoca.eCommerceApp.repository.CartItemRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.CartRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.ProductRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    private final ProductService productService;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    public ResponseEntity<?> addProductToCart(Long productId, Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            ProductSaveDto productData = productService.findById(productId);

            Product product = new Product();
            product.setId(productData.id());
            product.setName(productData.name());
            product.setDescription(productData.description());
            product.setPrice(productData.price());
            product.setStock(productData.stock());

            Cart userCart = user.getCart();

            if (userCart == null) {
                Cart cart = new Cart();
                cart.setUser(user);
                cart.setQuantity(1);

                CartItem cartItem = new CartItem();
                cartItem.setProduct(product);
                cartItem.setQuantity(1);
                cartItem.setCart(cart);

                List<CartItem> cartItems = new ArrayList<>();
                cartItems.add(cartItem);

                cart.setCartItems(cartItems);

                cartRepository.save(cart);
            } else {
                CartItem existingCartItem = cartItemRepository.findByCartAndProduct(userCart, product);

                if (existingCartItem == null) {
                    CartItem cartItem = new CartItem();
                    cartItem.setProduct(product);
                    cartItem.setQuantity(1);
                    cartItem.setCart(userCart);

                    List<CartItem> cartItems = userCart.getCartItems();
                    cartItems.add(cartItem);

                    userCart.setCartItems(cartItems);
                } else {
                    if (existingCartItem.getQuantity() < 4) {
                        int newQuantity = Math.min(existingCartItem.getQuantity() + 1, 4);
                        existingCartItem.setQuantity(newQuantity);
                    } else {
                        return ResponseEntity.badRequest().body("Bu üründen maksimum 4 adet ekleyebilirsiniz.");
                    }
                }
            }

            userRepository.save(user);

            return ResponseEntity.ok(productData);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }



    public ResponseEntity<ProductSaveDto> updateCart(Long cartItemId, int newQuantity) {
        try {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new RuntimeException("Sepet bulunamadı"));

            int updatedQuantity = Math.min(newQuantity, 4);
            cartItem.setQuantity(updatedQuantity);
            cartItemRepository.save(cartItem);

            Product product = cartItem.getProduct();
            return ResponseEntity.ok(new ProductSaveDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ResponseEntity<String> decreaseCartItemQuantity(Long userId, Long productId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            Cart userCart = user.getCart();

            if (userCart != null) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Ürün Bulunamadı"));
                CartItem cartItem = cartItemRepository.findByCartUserAndProduct(user, product);

                if (cartItem != null) {
                    int newQuantity = Math.max(cartItem.getQuantity() - 1, 0);
                    cartItem.setQuantity(newQuantity);

                    if (newQuantity == 0) {
                        cartItemRepository.delete(cartItem);
                        return ResponseEntity.ok("Ürün sepetten çıkarıldı");
                    }

                    cartItemRepository.save(cartItem);
                    return ResponseEntity.ok("Sepetteki ürün miktarı başarıyla azaldı");
                } else {
                    return ResponseEntity.badRequest().body("Belirtilen ürün kullanıcının sepetinde bulunamadı.");
                }
            } else {
                return ResponseEntity.badRequest().body("Kullanıcının bir sepeti bulunmamaktadır.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Sepette ürün miktarı azaltılırken hata oluştu");
        }
    }


    public ResponseEntity<?> removeProductFromUserCart(Long userId, Long productId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            Cart userCart = user.getCart();

            if (userCart != null) {
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Ürün Bulunamadı"));

                CartItem cartItem = cartItemRepository.findByCartUserAndProduct(user, product);

                if (cartItem != null) {
                    cartItemRepository.delete(cartItem);
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.badRequest().body("Belirtilen ürün kullanıcının sepetinde bulunamadı.");
                }
            } else {
                return ResponseEntity.badRequest().body("Kullanıcının bir sepeti bulunmamaktadır.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }


    public ResponseEntity<Void> emptyCart(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            Cart userCart = user.getCart();
            if (userCart != null) {
                cartItemRepository.deleteAll(userCart.getCartItems());

                userCart.getCartItems().clear();

                cartRepository.save(userCart);
            }

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Sepet silinirken hata oluştu");
        }
    }

    public CartSummaryDto getCart(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            Cart userCart = user.getCart();

            if (userCart == null || userCart.getCartItems().isEmpty()) {
                return new CartSummaryDto(Collections.emptyList(), 0.0);
            }

            List<ProductCartDto> productsInCart = userCart.getCartItems().stream()
                    .map(cartItem -> {
                        Product product = cartItem.getProduct();
                        return new ProductCartDto(
                                product.getId(),
                                product.getName(),
                                product.getDescription(),
                                product.getPrice(),
                                product.getStock(),
                                cartItem.getQuantity()
                        );
                    })
                    .collect(Collectors.toList());

            double totalCartPrice = productsInCart.stream()
                    .mapToDouble(product -> product.price() * product.quantity())
                    .sum();

            return new CartSummaryDto(productsInCart, totalCartPrice);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Sepet görüntülenirken hata oluştu");
        }
    }



}
