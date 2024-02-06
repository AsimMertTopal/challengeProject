package AsimMertTopal.enoca.eCommerceApp.controller;

import AsimMertTopal.enoca.eCommerceApp.dto.order.OrderDto;
import AsimMertTopal.enoca.eCommerceApp.dto.order.UsersOrderDto;
import AsimMertTopal.enoca.eCommerceApp.service.OrderService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@Data
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/placeOrder/{userId}")
    public ResponseEntity<String> placeOrder(
            @PathVariable Long userId,
            @RequestBody OrderDto orderDto
    ) {
        return orderService.placeOrder(userId, orderDto);
    }

    @GetMapping("/getAllOrdersForCustomer/{userId}")
    public List<UsersOrderDto> getAllOrdersForCustomer(
            @PathVariable Long userId
    ) {
        return orderService.getAllOrdersForCustomer(userId);
    }

    @GetMapping("/getOrderForCode/{userId}/{orderId}")
    public UsersOrderDto getOrderForCode(
            @PathVariable Long userId,
            @PathVariable Long orderId
    ) {
        return orderService.getOrderForCode(userId, orderId);
    }

}
