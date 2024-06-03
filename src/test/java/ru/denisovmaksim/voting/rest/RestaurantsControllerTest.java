package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.dto.RestaurantTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.RestaurantsController.RESTAURANTS;
import static ru.denisovmaksim.voting.utils.JsonUtils.fromJson;

public class RestaurantsControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RestaurantsRepository repository;

    @Test
    @DisplayName("Get all restaurants available for authorized users.")
    @WithMockUser
    public void testGetAll() throws Exception {
        final List<RestaurantTO> expected = repository.findAll()
                .stream()
                .map(restaurant -> new RestaurantTO(restaurant.getId(), restaurant.getName()))
                .collect(Collectors.toList());

        final var response = perform(get(RESTAURANTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<RestaurantTO> restaurants = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(restaurants)
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }

    @Test
    @DisplayName("Get all for unauthorized user should return 401.")
    @WithAnonymousUser
    public void testGetAllByUnauthorized() throws Exception {
        perform(get(RESTAURANTS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get one restaurant available for authorized users.")
    @WithMockUser
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final var response = perform(
                get(RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final RestaurantTO actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Get one restaurant for unauthorized user should return 401.")
    @WithAnonymousUser
    public void testGetOneByUnauthorized() throws Exception {
        final Restaurant expected = repository.findAll().get(0);

        perform(get(RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isUnauthorized());
    }
}
