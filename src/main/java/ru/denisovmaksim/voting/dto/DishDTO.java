package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Schema(description = "Dish")
public class DishDTO {
    @Schema(description = "Id of dish.")
    private Long id;
    @Schema(description = "Name of dish.")
    private String name;
    @Schema(description = "Price of dish in cents.")
    private Integer price;
}
