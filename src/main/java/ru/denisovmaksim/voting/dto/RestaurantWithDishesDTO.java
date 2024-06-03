package ru.denisovmaksim.voting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.denisovmaksim.voting.model.Dish;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestaurantWithDishesDTO {
    private Long id;
    private String name;

    private List<Dish> dishes;

    public RestaurantWithDishesDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
