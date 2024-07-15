package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.RestaurantDTO;
import ru.denisovmaksim.voting.dto.RestaurantWithDishesDTO;
import ru.denisovmaksim.voting.mapper.RestaurantsMapper;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RestaurantsService {
    private final RestaurantsRepository repository;
    private final RestaurantsMapper mapper;
    public List<RestaurantDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<RestaurantWithDishesDTO> getAllWithDishes() {
        return repository.findAll().stream()
                .map(mapper::toDTOWithDishes)
                .collect(Collectors.toList());
    }

    public RestaurantDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDTO)
                .orElseThrow();
    }

    public RestaurantWithDishesDTO getByIdWithDishes(Long id) {
        return repository.findById(id)
                .map(mapper::toDTOWithDishes)
                .orElseThrow();
    }

    public RestaurantDTO create(RestaurantDTO restaurantDTO) {
        if (restaurantDTO.getId() != null) {
            return null;
        }
        return mapper.toDTO(repository.save(new Restaurant(null, restaurantDTO.getName())));
    }

    public RestaurantDTO update(Long id, RestaurantDTO restaurantDTO) {
        if (!restaurantDTO.getId().equals(id)) {
            return null;
        }
        Restaurant restaurant = repository.findById(id)
                .orElseThrow();
        restaurant.setName(restaurantDTO.getName());
        return mapper.toDTO(repository.save(restaurant));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
