package AsimMertTopal.enoca.eCommerceApp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@RequiredArgsConstructor
@Data
public class Order extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "total_amaount", nullable = false)
    private double totalAmount;

    @Column(name = "address", nullable = false)
    @NotBlank(message = "Adres alanı boş bırakılamaz")
    private String address;

    @Column(name = "phone_number", nullable = false)
    @NotBlank(message = "Telefon numarası boş bırakılamaz")
    private String phoneNumber;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "İsim alanı boş bırakılamaz")
    private String name;

    @Column(name = "surname", nullable = false)
    @NotBlank(message = "Soyad alanı boş bırakılamaz")
    private String surname;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "Şehir alanı boş bırakılamaz")
    private String city;

    @Column(name = "card_number", nullable = false)
    private Integer cardNumber;

    @Column(name = "cvv", nullable = false)
    @Max(value = 3,message = "CVV 3 haneli olmalıdır")
    private Integer cvv;

    @Column(name = "order_date", nullable = false)
    private LocalDateTime orderDate;

    @Column(name = "order_price", nullable = false)
    private double orderPrice;

}
