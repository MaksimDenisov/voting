package ru.denisovmaksim.voting.rest.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.dto.RestaurantTO;
import ru.denisovmaksim.voting.dto.RestaurantWithDishesDTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.service.RestaurantsService;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RequestMapping("${base-url}")
@Slf4j
@RestController
public class AdminRestaurantsController {
    public static final String ADMIN_RESTAURANTS = "/admin/restaurants";
    public static final String ID = "/{id}";

    private final RestaurantsService service;

    @GetMapping(ADMIN_RESTAURANTS)
    public List<RestaurantWithDishesDTO> getAll() {
        return service.getAll()
                .stream()
                .map(r -> new RestaurantWithDishesDTO(r.getId(), r.getName(), r.getDishes()))
                .collect(Collectors.toList());
    }

    @GetMapping(ADMIN_RESTAURANTS + ID)
    public RestaurantWithDishesDTO getOne(@PathVariable("id") Long id) {
        Restaurant restaurant = service.getById(id);
        return new RestaurantWithDishesDTO(restaurant.getId(), restaurant.getName(), restaurant.getDishes());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(ADMIN_RESTAURANTS)
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurant create(@RequestBody @Valid RestaurantTO restaurant) {
        return service.create(restaurant);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(ADMIN_RESTAURANTS + ID)
    public Restaurant update(@PathVariable("id") Long id, @Valid @RequestBody RestaurantTO restaurant) {
        return service.update(id, restaurant);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(ADMIN_RESTAURANTS + ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
