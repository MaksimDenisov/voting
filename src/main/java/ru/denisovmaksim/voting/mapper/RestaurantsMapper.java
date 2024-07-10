package ru.denisovmaksim.voting.mapper;

import org.mapstruct.Mapper;
import ru.denisovmaksim.voting.dto.RestaurantDTO;
import ru.denisovmaksim.voting.dto.RestaurantWithDishesDTO;
import ru.denisovmaksim.voting.model.Restaurant;

@Mapper(componentModel = "spring", uses = { DishMapper.class })
public interface RestaurantsMapper {
   RestaurantDTO toDTO(Restaurant restaurant);

   RestaurantWithDishesDTO toDTOWithDishes(Restaurant restaurant);
}
