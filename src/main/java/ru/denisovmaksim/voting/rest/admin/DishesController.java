package ru.denisovmaksim.voting.rest.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Dishes of restaurant available for admins.")
public class DishesController {
    public static final String RESTAURANT_ID = "/{restaurant-id}";
    public static final String DISHES_PATH = "/admin" + RESTAURANTS_PATH + RESTAURANT_ID + "/dishes";
    public static final String ID = "/{id}";

    private final DishesService service;

    @GetMapping(DISHES_PATH)
    @Operation(summary = "Getting all dishes by restaurant.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = DishDTO.class))
    ))
    public List<DishDTO> getAllByRestaurantIdWithRestaurant(
            @Parameter(name = "restaurant-id", description = "Restaurant id", example = "1")
            @PathVariable("restaurant-id") Long restaurantId) {
        return service.getAllByRestaurantId(restaurantId);
    }

    @GetMapping(DISHES_PATH + ID)
    @Operation(summary = "Getting one dish by id and restaurant id.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = DishDTO.class))
    ))
    public DishDTO getById(
            @Parameter(name = "restaurant-id", description = "Restaurant id", example = "1")
            @PathVariable("restaurant-id") Long restaurantId,
            @Parameter(name = "id", description = "Dish id", example = "1")
            @PathVariable("id") Long dishId) {
        return service.getById(restaurantId, dishId);
    }

    @PostMapping(DISHES_PATH)
    @ResponseStatus(CREATED)
    @Operation(summary = "Create dish.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = DishDTO.class))
    ))
    public DishDTO create(
            @Parameter(name = "restaurant-id", description = "Restaurant id", example = "1")
            @PathVariable("restaurant-id") Long restaurantId,
                          @Valid @RequestBody DishDTO dishDTO) {
        return service.create(restaurantId, dishDTO);
    }

    @PutMapping(DISHES_PATH + ID)
    @Operation(summary = "Update dish.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = DishDTO.class))
    ))
    public DishDTO update(
            @Parameter(name = "restaurant-id", description = "Restaurant id", example = "1")
            @PathVariable("restaurant-id") Long restaurantId,
            @Parameter(name = "id", description = "Dish id", example = "1")
                          @PathVariable("id") Long dishId,
                          @Valid @RequestBody DishDTO dishDTO) {
        return service.update(restaurantId, dishId, dishDTO);
    }

    @DeleteMapping(DISHES_PATH + ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete restaurant by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", description = "Not found - The dish was not found")
    })
    public void delete(@PathVariable("restaurant-id") Long restaurantId, @PathVariable("id") Long dishId) {
        service.delete(restaurantId, dishId);
    }
}
