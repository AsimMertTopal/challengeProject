package AsimMertTopal.enoca.eCommerceApp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@RequiredArgsConstructor
@Data
public class Product extends BaseEntity {


    @Column(name = "product_name",nullable = false)
    @NotBlank(message = "Ürün adı boş bırakılamaz")
    private String name;

    @Column(name = "product_description",nullable = false)
    @NotBlank(message = "Ürün açıklaması boş bırakılamaz")
    private String description;

    @Column(name = "product_price",nullable = false)
    @NotBlank(message = "Ürün fiyatı boş bırakılamaz")
    private double price;

    @Column(name = "product_stock",nullable = false)
    @NotBlank(message = "Lütfen Ürün Stok Sayısını Belirtiniz")
    private double stock;


    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> order = new ArrayList<>();


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItem> cartItems;


}
