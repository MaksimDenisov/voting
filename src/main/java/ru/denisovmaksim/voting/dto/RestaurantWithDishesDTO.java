package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Schema(description = "Restaurant")
public class RestaurantWithDishesDTO {
    @Schema(description = "Id of restaurant.")
    private Long id;

    @Schema(description = "Name of restaurant.")
    private String name;

    @Schema(description = "Dishes of restaurant.")
    private List<DishDTO> dishes;

    public RestaurantWithDishesDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
