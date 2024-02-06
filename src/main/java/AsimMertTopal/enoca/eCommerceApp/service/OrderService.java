package AsimMertTopal.enoca.eCommerceApp.service;

import AsimMertTopal.enoca.eCommerceApp.dto.cart.CartSummaryDto;
import AsimMertTopal.enoca.eCommerceApp.dto.cart.ProductCartDto;
import AsimMertTopal.enoca.eCommerceApp.dto.order.OrderDto;
import AsimMertTopal.enoca.eCommerceApp.dto.order.UsersOrderDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductInfoDto;
import AsimMertTopal.enoca.eCommerceApp.entities.Order;
import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import AsimMertTopal.enoca.eCommerceApp.repository.OrderRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.ProductRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Data
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    private final ProductRepository  productRepository;


    public ResponseEntity<String> placeOrder(Long userId, OrderDto dto) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            CartSummaryDto cartSummary = cartService.getCart(userId);
            List<ProductCartDto> productsInCartDto = cartSummary.productsInCart();

            if (productsInCartDto.isEmpty()) {
                return ResponseEntity.badRequest().body("Sepet boş. Sipariş oluşturulamaz.");
            }

            for (ProductCartDto productInCart : productsInCartDto) {
                Product product = productRepository.findById(productInCart.id())
                        .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

                if (product.getStock() < productInCart.quantity() || product.getStock() == 0) {
                    return ResponseEntity.badRequest().body("Sipariş vermeye çalıştığınız " + product.getName() + " adlı ürünün stok miktarı yetersiz. Sipariş oluşturulamaz.");
                }

                double orderPrice = product.getPrice();

                Order orderItems = new Order();
                orderItems.setProduct(product);
                orderItems.setUser(user);
                orderItems.setQuantity(productInCart.quantity());
                orderItems.setTotalAmount(productInCart.price() * productInCart.quantity());
                orderItems.setAddress(dto.address());
                orderItems.setPhoneNumber(dto.phoneNumber());
                orderItems.setOrderPrice(orderPrice);
                orderItems.setName(dto.name());
                orderItems.setSurname(dto.surname());
                orderItems.setCity(dto.city());
                orderItems.setCardNumber(dto.cardNumber());
                orderItems.setCvv(dto.cvv());
                orderItems.setOrderDate(LocalDateTime.now());
                orderItems.setUser(user);

                int productStock = (int) (product.getStock() - productInCart.quantity());
                product.setStock(productStock);
                productRepository.save(product);

                orderRepository.save(orderItems);
            }

            cartService.emptyCart(userId);

            return ResponseEntity.ok("Sipariş başarıyla oluşturuldu");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Sipariş oluşturulurken bir hata oluştu");
        }
    }


    public List<UsersOrderDto>  getAllOrdersForCustomer(Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            List<Order> userOrderItems = orderRepository.findByUser(user);

            if (userOrderItems.isEmpty()) {
                return Collections.emptyList();
            }

            List<UsersOrderDto> orderDtoList = new ArrayList<>();

            for (Order orderItems : userOrderItems) {
                List<ProductInfoDto> productsInOrderDto = List.of(new ProductInfoDto(
                        orderItems.getProduct().getId(),
                        orderItems.getProduct().getName(),
                        orderItems.getProduct().getDescription(),
                        orderItems.getProduct().getPrice(),
                        orderItems.getProduct().getStock(),
                        orderItems.getOrderPrice()
                ));

                UsersOrderDto orderDto = new UsersOrderDto(
                        orderItems.getId(),
                        orderItems.getUser().getId(),
                        orderItems.getProduct().getId(),
                        orderItems.getTotalAmount(),
                        orderItems.getAddress(),
                        orderItems.getPhoneNumber(),
                        orderItems.getName(),
                        orderItems.getSurname(),
                        orderItems.getCity(),
                        orderItems.getOrderDate(),
                        productsInOrderDto
                );

                orderDtoList.add(orderDto);
            }

            return orderDtoList;
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Kullanıcının siparişleri ya da Kullanıcı bulunamadı");
        } catch (DataAccessException e) {
            throw new RuntimeException("Ürün getirilirken bir sorun oluştu");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Kullanıcının siparişleri getirilirken bir hata oluştu");
        }
    }

    public UsersOrderDto getOrderForCode(Long userId, Long orderId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("Kullanıcı Bulunamadı"));

            Order orderItem = orderRepository.findByIdAndUser(orderId, user)
                    .orElseThrow(() -> new RuntimeException("Sipariş bulunamadı"));

            List<ProductInfoDto> productsInOrderDto = List.of(new ProductInfoDto(
                    orderItem.getProduct().getId(),
                    orderItem.getProduct().getName(),
                    orderItem.getProduct().getDescription(),
                    orderItem.getProduct().getPrice(),
                    orderItem.getProduct().getStock(),
                    orderItem.getOrderPrice()
            ));

            return new UsersOrderDto(
                    orderItem.getId(),
                    orderItem.getUser().getId(),
                    orderItem.getProduct().getId(),
                    orderItem.getTotalAmount(),
                    orderItem.getAddress(),
                    orderItem.getPhoneNumber(),
                    orderItem.getName(),
                    orderItem.getSurname(),
                    orderItem.getCity(),
                    orderItem.getOrderDate(),
                    productsInOrderDto
            );
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Kullanıcının siparişi ya da Kullanıcı bulunamadı");
        } catch (DataAccessException e) {
            throw new RuntimeException("Sipariş getirilirken bir sorun oluştu");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Sipariş getirilirken bir hata oluştu");
        }
    }


}
