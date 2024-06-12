package ru.denisovmaksim.voting.rest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.dto.RestaurantTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.service.RestaurantsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}")
@Slf4j
public class RestaurantsController {
    public static final String RESTAURANTS_PATH = "/restaurants";
    public static final String ID = "/{id}";

    private final RestaurantsService service;

    @GetMapping(RESTAURANTS_PATH)
    public List<RestaurantTO> getAll() {
        return service.getAll()
                .stream()
                .map(r -> new RestaurantTO(r.getId(), r.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping(RESTAURANTS_PATH + ID)
    public RestaurantTO getOne(@PathVariable("id") Long id) {
        Restaurant restaurant = service.getById(id);
        return new RestaurantTO(restaurant.getId(), restaurant.getName());
    }

}
