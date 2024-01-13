package ru.denisovmaksim.voting.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
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
}
