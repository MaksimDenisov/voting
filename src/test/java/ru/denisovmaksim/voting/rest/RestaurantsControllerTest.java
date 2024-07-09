package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.dto.RestaurantDTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.RestaurantsController.RESTAURANTS_PATH;

public class RestaurantsControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RestaurantsRepository repository;

    @Test
    @DisplayName("User: Get all restaurants.")
    @WithMockUser
    public void testGetAll() throws Exception {
        final List<RestaurantDTO> expected = repository.findAll()
                .stream()
                .map(restaurant -> new RestaurantDTO(restaurant.getId(), restaurant.getName()))
                .collect(Collectors.toList());

        final var response = perform(get(RESTAURANTS_PATH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<RestaurantDTO> restaurants = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(restaurants)
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }

    @Test
    @DisplayName("Anonymous: Get all is unauthorized.")
    @WithAnonymousUser
    public void testGetAllByUnauthorized() throws Exception {
        perform(get(RESTAURANTS_PATH))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("User: Get one restaurant.")
    @WithMockUser
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final var response = perform(
                get(RESTAURANTS_PATH + ID, expected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final RestaurantDTO actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Anonymous: Get one restaurant is unauthorized.")
    @WithAnonymousUser
    public void testGetOneByUnauthorized() throws Exception {
        final Restaurant expected = repository.findAll().get(0);

        perform(get(RESTAURANTS_PATH + ID, expected.getId()))
                .andExpect(status().isUnauthorized());
    }
}
