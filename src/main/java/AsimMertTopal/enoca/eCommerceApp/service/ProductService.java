package AsimMertTopal.enoca.eCommerceApp.service;

import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductSaveDto;
import AsimMertTopal.enoca.eCommerceApp.dto.product.ProductUpdateDto;
import AsimMertTopal.enoca.eCommerceApp.entities.Product;
import AsimMertTopal.enoca.eCommerceApp.entities.User;
import AsimMertTopal.enoca.eCommerceApp.repository.ProductRepository;
import AsimMertTopal.enoca.eCommerceApp.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public boolean isUserSeller(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Geçersiz kullanıcı ID: " + userId));

        List<String> roles = Collections.singletonList(user.getRoles());

        return roles != null && roles.stream().anyMatch(role -> role.equals("ROLE_SELLER"));
    }



    public ResponseEntity<String> createProduct(ProductSaveDto dto, Long userId) {
        try {
            if (!isUserSeller(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bu işlemi gerçekleştirmek için satıcı rolüne sahip olmalısınız.");
            }

            Product product = new Product();
            product.setName(dto.name());
            product.setDescription(dto.description());
            product.setPrice(dto.price());
            product.setStock(dto.stock());



            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz kullanıcı ID: " + userId));
            product.setUser(user);

            Product savedProduct = productRepository.save(product);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Ürün başarıyla eklendi. Ürün ID: " + savedProduct.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ürün eklenirken bir hata oluştu");
        }
    }

    public ResponseEntity<String> updateProduct(Long productId, ProductUpdateDto dto, Long userId) {
        try {
            if (!isUserSeller(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bu işlemi gerçekleştirmek için satıcı rolüne sahip olmalısınız.");
            }

            Product existingProduct = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("Geçersiz ürün ID: " + productId));

            if (!existingProduct.getUser().getId().equals(userId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Bu işlemi gerçekleştirmek için kendi ürününüzü güncellemelisiniz.");
            }

            existingProduct.setName(dto.name());
            existingProduct.setDescription(dto.description());
            existingProduct.setPrice(dto.price());
            existingProduct.setStock(dto.stock());

            Product updatedProduct = productRepository.save(existingProduct);

            return ResponseEntity.status(HttpStatus.OK)
                    .body("Ürün başarıyla güncellendi. Güncellenen Ürün ID: " + updatedProduct.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ürün güncellenirken bir hata oluştu");
        }
    }

    public ResponseEntity<String> deleteProduct (Long productId, Long userId) {
            try {
                if (!isUserSeller(userId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body("Bu işlemi gerçekleştirmek için satıcı rolüne sahip olmalısınız.");
                }

                productRepository.deleteById(productId);
                return ResponseEntity.status(HttpStatus.OK)
                        .body("Ürün Başrıyla Silindi.");
    }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ürün silinirken bir hata olustu");
        }
    }


    public List<ProductSaveDto> getAll() {
        try {
            List<Product> products = productRepository.findAll();

            return products.stream()
                    .filter(product -> product.getStock() > 0)
                    .map(product -> new ProductSaveDto(
                            product.getId(),
                            product.getName(),
                            product.getDescription(),
                            product.getPrice(),
                            product.getStock()
                    ))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Ürünler getirilirken bir hata oldu");
        }
    }




    public ProductSaveDto findById(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElse(null);

            if (product == null) {
                throw new RuntimeException("Bu ID de bir ürün yok.");
            }

            return new ProductSaveDto(
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock()
            );
        } catch (Exception e) {
            throw new RuntimeException("Ürün getirilirken bir hata oluştu");
        }
    }

}
