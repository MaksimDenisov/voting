package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Restaurant")
public class RestaurantDTO {
    @Schema(description = "Id of restaurant.")
    private  Long id;

    @NotBlank
    @Schema(description = "Name of restaurant.")
    private  String name;
}
