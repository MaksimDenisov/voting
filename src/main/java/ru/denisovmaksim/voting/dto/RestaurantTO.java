package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Restaurant")
public class RestaurantTO {
    @Schema(description = "Id of restaurant.")
    private  Long id;

    @NotBlank
    @Schema(description = "Name of restaurant.")
    private  String name;
}
