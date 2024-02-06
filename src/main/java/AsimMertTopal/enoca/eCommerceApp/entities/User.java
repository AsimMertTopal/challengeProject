package AsimMertTopal.enoca.eCommerceApp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Table(name = "users")
@Data
@RequiredArgsConstructor
public class User extends BaseEntity {

    @Column(name = "username", unique = true)
    @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
    @UniqueElements(message = "Bu kullanıcı adı zaten kullanılıyor")
    private String username;

    @Column(name = "email",  unique = true)
    @Email(message = "E mail formatı yanlış ")
    @UniqueElements(message = "Bu email zaten kullanılıyor")
    @NotBlank(message = "Lütfen E-mail Giriniz")
    private String email;

    @Column(name = "password")
    @NotBlank(message = "Lütfen Şifrenizi Giriniz")
    private String password;

    @Column(name = "role", nullable = false)
    private String roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;




}
