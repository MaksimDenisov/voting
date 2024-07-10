package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.mapper.DishMapper;
import ru.denisovmaksim.voting.model.Dish;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishesService {
    private final DishesRepository dishesRepository;
    private final RestaurantsRepository restaurantsRepository;
    private final DishMapper mapper;

    public List<DishDTO> getAllByRestaurantId(Long restaurantId) {
        return dishesRepository.getAllByRestaurantId(restaurantId)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public DishDTO getById(Long restaurantId, Long dishId) {
        return mapper.toDTO(dishesRepository.getById(restaurantId, dishId));
    }

    public DishDTO create(Long restaurantId, DishDTO dishDTO) {
        if (dishDTO.getId() != null) {
            return null;
        }
        Dish saved = mapper.fromDTO(dishDTO);
        saved.setRestaurant(restaurantsRepository.getReferenceById(restaurantId));
        return mapper.toDTO(dishesRepository.save(saved));
    }

    public DishDTO update(Long restaurantId, Long dishId, DishDTO dishDTO) {
        Dish dish = dishesRepository.getById(restaurantId, dishId);
        if (dish == null) {
            return null;
        }
        dish.setName(dishDTO.getName());
        dish.setPrice(dishDTO.getPrice());
        return mapper.toDTO(dishesRepository.save(dish));
    }

    public void delete(Long restaurantId, Long dishId) {
        dishesRepository.delete(restaurantId, dishId);
    }
}
