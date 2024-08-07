package ru.denisovmaksim.voting.rest.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.dto.RestaurantWithDishesDTO;
import ru.denisovmaksim.voting.mapper.DishMapper;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.rest.AbstractMockMvcTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.admin.AdminRestaurantsController.ADMIN_RESTAURANTS;

class AdminRestaurantsControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RestaurantsRepository repository;
    @Autowired
    private DishesRepository dishesRepository;
    @Autowired
    private DishMapper dishMapper;
    @Test
    @DisplayName("Admin: Get all restaurants with dishes.")
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAllWithDishes() throws Exception {
        final List<RestaurantWithDishesDTO> expected = repository.findAll()
                .stream()
                .map(restaurant -> new RestaurantWithDishesDTO(restaurant.getId(), restaurant.getName()))
                .collect(Collectors.toList());

        final var response = perform(get(ADMIN_RESTAURANTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<RestaurantWithDishesDTO> restaurants = fromJson(response.getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(restaurants)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("dishes")
                .containsAll(expected);

        RestaurantWithDishesDTO restaurantWithDishesDTO = restaurants.stream()
                .filter(r -> r.getId().equals(expected.get(0).getId()))
                .findFirst().orElseThrow();
        final List<DishDTO> expectedDishes = dishesRepository.getAllByRestaurantId(expected.get(0).getId())
                .stream()
                .map(dishMapper::toDTO)
                .collect(Collectors.toList());

        assertThat(new ArrayList<>(restaurantWithDishesDTO.getDishes()))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("restaurant")
                .containsAll(expectedDishes);
    }

    @Test
    @DisplayName("User : Get all restaurants is forbidden.")
    @WithMockUser
    public void testGetAllByUser() throws Exception {
        checkRequestIsForbidden(get(ADMIN_RESTAURANTS));
    }

    @Test
    @DisplayName("Admin: Get one restaurant with dishes.")
    @WithMockUser(roles = {"ADMIN"})
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final List<DishDTO> expectedDishes = dishesRepository.getAllByRestaurantId(expected.getId())
                .stream()
                .map(dishMapper::toDTO)
                .collect(Collectors.toList());

        final var response = perform(
                get(ADMIN_RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn()
                .getResponse();

        final RestaurantWithDishesDTO actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(expected);

        assertThat(new ArrayList<>(actual.getDishes()))
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("restaurant")
                .containsAll(expectedDishes);
    }

    @Test
    @DisplayName("User: Get one restaurant with dishes is forbidden.")
    @WithMockUser
    public void testGetOneByUnauthorized() throws Exception {
        final Restaurant expected = repository.findAll().get(0);

        checkRequestIsForbidden(get(ADMIN_RESTAURANTS + ID, expected.getId()));
    }

    @Test
    @DisplayName("Admin: Create restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreate() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        final var response = perform(post(ADMIN_RESTAURANTS)
                .content(asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final Restaurant result = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Restaurant actual = repository.findById(result.getId()).orElse(null);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id", "dishes")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Admin: Create invalid restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateInvalid() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("");

        perform(post(ADMIN_RESTAURANTS)
                .content(asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Admin: Update restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        final var response = perform(put(ADMIN_RESTAURANTS + ID, expectedId)
                .content(asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final Restaurant result = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Restaurant actual = repository.findById(result.getId()).orElse(null);

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("dishes")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Admin: Delete restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testDelete() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        perform(delete(ADMIN_RESTAURANTS + ID, expectedId))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(expectedId).isPresent()).isFalse();
    }

    @Test
    @DisplayName("User: Create restaurant is forbidden.")
    @WithMockUser
    public void testCreateByUser() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");

        checkRequestIsForbidden(post(ADMIN_RESTAURANTS)
                .content(asJson(expected)));
    }

    @Test
    @DisplayName("User: Delete restaurant is forbidden.")
    @WithMockUser
    public void testDeleteByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        checkRequestIsForbidden(delete(ADMIN_RESTAURANTS + ID, expectedId));
    }

    @Test
    @DisplayName("User: Update restaurant is forbidden.")
    @WithMockUser
    public void testUpdateByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        final Restaurant expected = new Restaurant(expectedId, "Updated name");

        checkRequestIsForbidden(put(ADMIN_RESTAURANTS + ID, expectedId)
                .content(asJson(expected)));
    }
}
