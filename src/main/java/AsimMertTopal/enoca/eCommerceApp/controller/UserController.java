package AsimMertTopal.enoca.eCommerceApp.controller;

import AsimMertTopal.enoca.eCommerceApp.dto.user.UserCreateDto;
import AsimMertTopal.enoca.eCommerceApp.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/user")
@Data
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody UserCreateDto userCreateDto) {
        return userService.addCustomer(userCreateDto);
    }

    @PostMapping("/addSeller")
    public ResponseEntity<String> addSeller(@RequestBody UserCreateDto userCreateDto) {
        return userService.addSeller(userCreateDto);
    }
}
