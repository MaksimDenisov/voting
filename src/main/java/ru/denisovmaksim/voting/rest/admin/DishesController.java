package ru.denisovmaksim.voting.rest.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.dto.DishDTO;

import java.util.List;

import static ru.denisovmaksim.voting.rest.RestaurantsController.RESTAURANTS;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}" + RESTAURANTS)
@Slf4j
public class DishesController {

    public static final String RESTAURANT_ID = "/{restaurant-id}";
    public static final String DISHES = RESTAURANTS + RESTAURANT_ID + "/dishes";
    public static final String ID = "/{id}";
    @GetMapping(DISHES)
    public List<DishDTO> getAllByRestaurantId(@PathVariable("restaurant-id") Long restaurantId) {
        return null;
    }

    @GetMapping(DISHES + ID)
    public DishDTO getById(@PathVariable("restaurant-id") Long restaurantId, @PathVariable(ID) Long dishId) {
        return null;
    }

    @PostMapping(DISHES)
    public DishDTO create(@RequestParam DishDTO dishDTO) {
        return null;
    }

    @PutMapping(DISHES)
    public DishDTO update(@PathVariable("restaurant-id") Long restaurantId, @RequestParam DishDTO dishDTO) {
        return null;
    }

    @DeleteMapping(DISHES + ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restaurant-id") Long restaurantId, @PathVariable("id`") Long dishId) {

    }
}
