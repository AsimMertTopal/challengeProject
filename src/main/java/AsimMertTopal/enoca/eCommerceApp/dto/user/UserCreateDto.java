package AsimMertTopal.enoca.eCommerceApp.dto.user;

public record UserCreateDto(
        String username,
        String email,
        String password,
        String roles
) {
}
