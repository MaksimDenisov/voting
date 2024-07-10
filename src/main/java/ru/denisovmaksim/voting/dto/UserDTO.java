package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;



@Data
@Schema(description = "User")
public class UserDTO {
    @Schema(description = "User's email")
    private String email;
}
