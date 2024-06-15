package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.model.Dish;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishesService {
    private final DishesRepository dishesRepository;
    private final RestaurantsRepository restaurantsRepository;

    public List<DishDTO> getAllByRestaurantId(Long restaurantId) {
        return dishesRepository.getAllByRestaurantId(restaurantId)
                .stream()
                .map(dish -> new DishDTO(dish.getId(), dish.getName(), dish.getPrice()))
                .collect(Collectors.toList());
    }

    public DishDTO getById(Long restaurantId, Long dishId) {
        Dish dish = dishesRepository.getById(restaurantId, dishId);
        return new DishDTO(dish.getId(), dish.getName(), dish.getPrice());
    }

    public DishDTO create(Long restaurantId, DishDTO dishDTO) {
        if (dishDTO.getId() != null) {
            return null;
        }
        Restaurant restaurant = restaurantsRepository.getReferenceById(restaurantId);
        Dish dish = new Dish(null, dishDTO.getName(), dishDTO.getPrice(), restaurant);
        Dish result = dishesRepository.save(dish);
        return new DishDTO(result.getId(), result.getName(), result.getPrice());
    }

    public DishDTO update(Long restaurantId, Long dishId, DishDTO dishDTO) {
        Dish dish = dishesRepository.getById(restaurantId, dishId);
        if (dish == null) {
            return null;
        }
        dish.setName(dishDTO.getName());
        dish.setPrice(dishDTO.getPrice());
        Dish result = dishesRepository.save(dish);
        return new DishDTO(result.getId(), result.getName(), result.getPrice());
    }

    public void delete(Long restaurantId, Long dishId) {
        dishesRepository.delete(restaurantId, dishId);
    }
}
