package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.RestaurantDTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantsService {
    private final RestaurantsRepository repository;

    public List<Restaurant> getAll() {
        return repository.findAll();
    }

    public Restaurant getById(Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    public Restaurant create(RestaurantDTO restaurantDTO) {
        if (restaurantDTO.getId() != null) {
            return null;
        }
        Restaurant restaurant = new Restaurant(null, restaurantDTO.getName());
        return repository.save(restaurant);
    }

    public Restaurant update(Long id, RestaurantDTO restaurantDTO) {
        if (!restaurantDTO.getId().equals(id)) {
            return null;
        }
        Restaurant restaurant = getById(id);
        restaurant.setName(restaurantDTO.getName());
        return repository.save(restaurant);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
