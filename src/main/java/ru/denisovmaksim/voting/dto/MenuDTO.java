package ru.denisovmaksim.voting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Schema(description = "Menu")
public class MenuDTO {
    @Schema(description = "Date of menu")
    private LocalDate date;

    @Schema(description = "Restaurant")
    private RestaurantDTO restaurantDTO;

    @Schema(description = "List of dishes")
    private List<DishDTO> dishes;
}
