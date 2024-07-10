package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
@Schema(description = "User for signup")
public class UserCreationDTO {
    @Schema(description = "User's email.", example = "user@user.com")
    private String email;

    @Schema(description = "User's password.", example = "user-very-strong-pass")
    private String password;
}
