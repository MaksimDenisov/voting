package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Dish")
public class DishDTO {
    @Schema(description = "Id of dish.")
    private Long id;
    @Schema(description = "Name of dish.")
    private String name;
    @Schema(description = "Price of dish in cents.")
    private Integer price;
}
