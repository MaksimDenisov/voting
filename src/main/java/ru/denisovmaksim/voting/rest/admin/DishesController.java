package ru.denisovmaksim.voting.rest.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.service.DishesService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static ru.denisovmaksim.voting.rest.RestaurantsController.RESTAURANTS_PATH;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}")
@Slf4j
public class DishesController {
    public static final String RESTAURANT_ID = "/{restaurant-id}";
    public static final String DISHES_PATH = "/admin" + RESTAURANTS_PATH + RESTAURANT_ID + "/dishes";
    public static final String ID = "/{id}";

    private final DishesService service;

    @GetMapping(DISHES_PATH)
    public List<DishDTO> getAllByRestaurantIdWithRestaurant(@PathVariable("restaurant-id") Long restaurantId) {
        return service.getAllByRestaurantId(restaurantId);
    }

    @GetMapping(DISHES_PATH + ID)
    public DishDTO getById(@PathVariable("restaurant-id") Long restaurantId,
                           @PathVariable(ID) Long dishId) {
        return service.getById(restaurantId, dishId);
    }

    @PostMapping(DISHES_PATH)
    @ResponseStatus(CREATED)
    public DishDTO create(@PathVariable("restaurant-id") Long restaurantId,
                          @Valid @RequestBody DishDTO dishDTO) {
        return service.create(restaurantId, dishDTO);
    }

    @PutMapping(DISHES_PATH + ID)
    public DishDTO update(@PathVariable("restaurant-id") Long restaurantId,
                          @PathVariable("id") Long dishId,
                          @Valid @RequestBody DishDTO dishDTO) {
        return service.update(restaurantId, dishId, dishDTO);
    }

    @DeleteMapping(DISHES_PATH + ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurant-id") Long restaurantId, @PathVariable("id") Long dishId) {
        service.delete(restaurantId, dishId);
    }
}
