package ru.denisovmaksim.voting.mapper;

import org.mapstruct.Mapper;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.model.Dish;

@Mapper(componentModel = "spring")
public interface DishMapper {
    DishDTO toDTO(Dish dish);

    Dish fromDTO(DishDTO dish);
}
