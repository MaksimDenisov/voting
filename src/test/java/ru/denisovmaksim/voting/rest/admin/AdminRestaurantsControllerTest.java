package ru.denisovmaksim.voting.rest.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.dto.RestaurantWithDishesDTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.rest.AbstractMockMvcTest;
import ru.denisovmaksim.voting.utils.JsonUtils;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.admin.AdminRestaurantsController.ADMIN_RESTAURANTS;
import static ru.denisovmaksim.voting.utils.JsonUtils.fromJson;


class AdminRestaurantsControllerTest extends AbstractMockMvcTest {

    @Autowired
    private RestaurantsRepository repository;

    @Test
    @DisplayName("Get all restaurants with dishes available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testGetAll() throws Exception {
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
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }

    @Test
    @DisplayName("Get all restaurants with dishes is forbidden for regular user.")
    @WithMockUser
    public void testGetAllByUnauthorized() throws Exception {
        perform(get(ADMIN_RESTAURANTS))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get one restaurant with dishes available for admin.")
    @WithMockUser(roles = {"ADMIN"})
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final var response = perform(
                get(ADMIN_RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final RestaurantWithDishesDTO actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Get one restaurant with dishes is forbidden for regular user.")
    @WithMockUser
    public void testGetOneByUnauthorized() throws Exception {
        final Restaurant expected = repository.findAll().get(0);

        perform(get(ADMIN_RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Create restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreate() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        final var response = perform(post(ADMIN_RESTAURANTS)
                .content(JsonUtils.asJson(expected))
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
    @DisplayName("Create invalid restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateInvalid() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("");

        perform(post(ADMIN_RESTAURANTS)
                .content(JsonUtils.asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        final var response = perform(put(ADMIN_RESTAURANTS + ID, expectedId)
                .content(JsonUtils.asJson(expected))
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
    @DisplayName("Delete restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testDelete() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        perform(delete(ADMIN_RESTAURANTS + ID, expectedId))
                .andExpect(status().isNoContent());

        assertThat(repository.findById(expectedId).isPresent()).isFalse();
    }

    @Test
    @DisplayName("Create restaurant for user is forbidden.")
    @WithMockUser
    public void testCreateByUser() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");

        perform(post(ADMIN_RESTAURANTS)
                .content(JsonUtils.asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete restaurant unavailable for user.")
    @WithMockUser
    public void testDeleteByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        perform(delete(ADMIN_RESTAURANTS + ID, expectedId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update restaurant for user is forbidden.")
    @WithMockUser
    public void testUpdateByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        final Restaurant expected = new Restaurant(expectedId, "Updated name");

        perform(put(ADMIN_RESTAURANTS + ID, expectedId)
                .content(JsonUtils.asJson(expected))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }
}
