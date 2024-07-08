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
@Tag(name = "Restaurants available for admins.")
public class AdminRestaurantsController {
    public static final String ADMIN_RESTAURANTS = "/admin/restaurants";
    public static final String ID = "/{id}";

    private final RestaurantsService service;

    @GetMapping(ADMIN_RESTAURANTS)
    @Operation(summary = "Getting all restaurants with all their dishes.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = RestaurantWithDishesDTO.class))
    ))
    public List<RestaurantWithDishesDTO> getAll() {
        return service.getAll()
                .stream()
                .map(r -> new RestaurantWithDishesDTO(r.getId(), r.getName(), r.getDishes()))
                .collect(Collectors.toList());
    }

    @GetMapping(ADMIN_RESTAURANTS + ID)
    @Operation(summary = "Get one restaurant with all dishes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(schema = @Schema(implementation = RestaurantWithDishesDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found - The restaurant was not found")
    })
    public RestaurantWithDishesDTO getOne(@Parameter(name = "id", description = "Restaurant id", example = "1")
                                          @PathVariable("id") Long id) {
        Restaurant restaurant = service.getById(id);
        return new RestaurantWithDishesDTO(restaurant.getId(), restaurant.getName(), restaurant.getDishes());
    }

    @PostMapping(ADMIN_RESTAURANTS)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create restaurant.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = Restaurant.class))
    ))
    public Restaurant create(@RequestBody @Valid RestaurantTO restaurant) {
        return service.create(restaurant);
    }


    @PutMapping(ADMIN_RESTAURANTS + ID)
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update restaurant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", content =
            @Content(schema = @Schema(implementation = Restaurant.class))),
            @ApiResponse(responseCode = "404", description = "Not found - The restaurant was not found")
    })
    public Restaurant update(@Parameter(name = "id", description = "Restaurant id", example = "1")
                             @PathVariable("id") Long id, @Valid @RequestBody RestaurantTO restaurant) {
        return service.update(id, restaurant);
    }

    @DeleteMapping(ADMIN_RESTAURANTS + ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete restaurant by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "404", description = "Not found - The restaurant was not found")
    })
    public void delete(@Parameter(name = "id", description = "Restaurant id", example = "1")
                       @PathVariable("id") Long id) {
        service.delete(id);
    }
}
