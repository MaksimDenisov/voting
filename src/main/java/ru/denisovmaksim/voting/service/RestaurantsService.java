package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.RestaurantTO;
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

    public Restaurant create(RestaurantTO restaurantTO) {
        if (restaurantTO.getId() != null) {
            return null;
        }
        Restaurant restaurant = new Restaurant(null, restaurantTO.getName());
        return repository.save(restaurant);
    }

    public Restaurant update(Long id, RestaurantTO restaurantTO) {
        if (!restaurantTO.getId().equals(id)) {
            return null;
        }
        Restaurant restaurant = getById(id);
        restaurant.setName(restaurantTO.getName());
        return repository.save(restaurant);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
