package ru.denisovmaksim.voting.rest.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.dto.DishDTO;
import ru.denisovmaksim.voting.model.Dish;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.rest.AbstractMockMvcTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.admin.DishesController.DISHES_PATH;
import static ru.denisovmaksim.voting.rest.admin.DishesController.ID;

class DishesControllerTest extends AbstractMockMvcTest {
    private static final Long MEAT_RESTAURANT_ID = 1L;

    @Autowired
    private DishesRepository dishesRepository;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Test
    @DisplayName("Admin: Get dishes from one restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void getById() throws Exception {
        final List<DishDTO> expected = dishesRepository.getAllByRestaurantId(MEAT_RESTAURANT_ID)
                .stream()
                .map(dish -> new DishDTO(dish.getId(), dish.getName(), dish.getPrice()))
                .collect(Collectors.toList());

        final var response = perform(get(DISHES_PATH, MEAT_RESTAURANT_ID))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<DishDTO> actual = fromJson(response.getContentAsString(),
                new TypeReference<>() {
                });

        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }

    @Test
    @DisplayName("Admin: Create dish for restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void create() throws Exception {
        final DishDTO expected = new DishDTO(null, "New Dish", 100);
        final var response = perform(post(DISHES_PATH, MEAT_RESTAURANT_ID)
                .content(asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        final DishDTO result = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Dish dish = dishesRepository.
                findById(result.getId())
                .orElseThrow();
        final DishDTO actual = new DishDTO(dish.getId(), dish.getName(), dish.getPrice());

        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Admin: Update dish for restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void update() throws Exception {
        long expectedId = dishesRepository.getAllByRestaurantId(MEAT_RESTAURANT_ID).get(0).getId();
        DishDTO expected = new DishDTO(expectedId, "Updated name", 0);
        final var response = perform(put(DISHES_PATH + "/" + expectedId, MEAT_RESTAURANT_ID)
                .content(asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final DishDTO actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Admin: Delete dish from restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void testDelete() throws Exception {
        long expectedId = dishesRepository.getAllByRestaurantId(MEAT_RESTAURANT_ID).get(0).getId();

        perform(delete(DISHES_PATH + ID, MEAT_RESTAURANT_ID, expectedId))
                .andExpect(status().isNoContent());

        AssertionsForClassTypes.assertThat(dishesRepository.findById(expectedId).isPresent()).isFalse();
    }

    @Test
    @DisplayName("User: Get one restaurant with dishes is forbidden.")
    @WithMockUser
    public void getByIdByUser() throws Exception {
        Dish dish = dishesRepository.findAll().get(0);
        checkRequestIsForbidden(get(DISHES_PATH + ID, MEAT_RESTAURANT_ID, dish.getId()));
    }

    @Test
    @DisplayName("User: Create dish forbidden.")
    @WithMockUser
    public void createByUser() throws Exception {
        checkRequestIsForbidden(post(DISHES_PATH, MEAT_RESTAURANT_ID)
                .content(asJson(getNewDish()))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("User: Update dish forbidden.")
    @WithMockUser
    public void updateByUser() throws Exception {
        Dish updatedDish = getUpdatedDish();
        checkRequestIsForbidden(put(DISHES_PATH + ID, MEAT_RESTAURANT_ID, updatedDish.getId())
                .content(asJson(updatedDish))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("User: Delete dish forbidden.")
    @WithMockUser
    public void deleteByUser() throws Exception {
        Dish dish = dishesRepository.findAll().get(0);
        checkRequestIsForbidden(delete(DISHES_PATH + ID, MEAT_RESTAURANT_ID, dish.getId()));
    }

    private Dish getNewDish() {
        return new Dish(null, "New dish", 10000, restaurantsRepository.getReferenceById(MEAT_RESTAURANT_ID));
    }

    private Dish getUpdatedDish() {
        Dish dish = dishesRepository.findAll().get(0);
        dish.setName("Updated dish");
        return dish;
    }
}
