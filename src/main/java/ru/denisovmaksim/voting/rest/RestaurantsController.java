package ru.denisovmaksim.voting.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Restaurants available for users.")
public class RestaurantsController {
    public static final String RESTAURANTS_PATH = "/restaurants";
    public static final String ID = "/{id}";

    private final RestaurantsService service;

    @GetMapping(RESTAURANTS_PATH)
    @Operation(summary = "Getting all restaurants.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = RestaurantTO.class))
    ))
    public List<RestaurantTO> getAll() {
        return service.getAll()
                .stream()
                .map(r -> new RestaurantTO(r.getId(), r.getName()))
                .collect(Collectors.toList());
    }

    @GetMapping(RESTAURANTS_PATH + ID)
    @Operation(summary = "Get one restaurant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content =
            @Content(schema = @Schema(implementation = RestaurantTO.class))),
            @ApiResponse(responseCode = "404", description = "Not found - The restaurant was not found")
    }
    )
    public RestaurantTO getOne(@Parameter(name = "id", description = "Restaurant id", example = "1")
                               @PathVariable("id") Long id) {
        Restaurant restaurant = service.getById(id);
        return new RestaurantTO(restaurant.getId(), restaurant.getName());
    }

}
