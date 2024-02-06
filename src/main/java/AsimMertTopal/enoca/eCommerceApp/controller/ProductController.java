package AsimMertTopal.enoca.eCommerceApp.controller;

import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductSaveDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductUpdateDto;
import AsimMertTopal.enoca.eCommerceApp.service.ProductService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
@Data
public class ProductController {

    private final ProductService productService;

    @PostMapping("/createProduct/{userId}")
    public ResponseEntity<String> createProduct(
            @RequestBody ProductSaveDto productSaveDto,
            @PathVariable Long userId
    ) {
        return productService.createProduct(productSaveDto, userId);
    }

    @PutMapping("/updateProduct/{productId}/{userId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateDto productUpdateDto,
            @PathVariable Long userId
    ) {
        return productService.updateProduct(productId, productUpdateDto, userId);
    }

    @DeleteMapping("/deleteProduct/{productId}/{userId}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long productId,
            @PathVariable Long userId
    ) {
        return productService.deleteProduct(productId, userId);
    }

    @GetMapping("/getAllProducts")
    public List<ProductSaveDto> getAllProducts() {
        return productService.getAll();
    }

    @GetMapping("/getProduct/{productId}")
    public ProductSaveDto getProductById(
            @PathVariable Long productId
    ) {
        return productService.findById(productId);
    }
}
