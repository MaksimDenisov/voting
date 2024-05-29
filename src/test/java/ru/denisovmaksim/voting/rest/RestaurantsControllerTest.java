package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.denisovmaksim.voting.dto.RestaurantTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.utils.JsonUtils;
import ru.denisovmaksim.voting.utils.TestData;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.RestaurantsController.RESTAURANTS;
import static ru.denisovmaksim.voting.utils.JsonUtils.fromJson;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class RestaurantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantsRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        repository.saveAll(TestData.getRestaurants());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get all restaurants available for authorized users.")
    @WithMockUser
    public void testGetAll() throws Exception {
        final var response = mockMvc.perform(get(RESTAURANTS))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final List<RestaurantTO> restaurants = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final List<RestaurantTO> expected = repository.findAll()
                .stream()
                .map(restaurant -> new RestaurantTO(restaurant.getId(), restaurant.getName()))
                .collect(Collectors.toList());
        assertThat(restaurants)
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }

    @Test
    @DisplayName("Get all for unauthorized user should return 401.")
    @WithAnonymousUser
    public void testGetAllByUnauthorized() throws Exception {
        mockMvc.perform(get(RESTAURANTS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get one restaurant available for authorized users.")
    @WithMockUser
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final var response = mockMvc.perform(
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
        mockMvc.perform(get(RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreate() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        final var response = mockMvc.perform(post(RESTAURANTS)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        final Restaurant result = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Restaurant actual = repository.findById(result.getId()).orElse(null);

        assertNotNull(actual);
        assertThat(actual)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Create invalid restaurant.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateInvalid() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("");
        mockMvc.perform(post(RESTAURANTS)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create restaurant for user is forbidden.")
    @WithMockUser
    public void testCreateByUser() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        mockMvc.perform(post(RESTAURANTS)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        final var response = mockMvc.perform(put(RESTAURANTS + ID, expectedId)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final Restaurant result = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        final Restaurant actual = repository.findById(result.getId()).orElse(null);

        assertNotNull(actual);
        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Update restaurant for user is forbidden.")
    @WithMockUser
    public void testUpdateByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        mockMvc.perform(put(RESTAURANTS + ID, expectedId)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testDelete() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        mockMvc.perform(delete(RESTAURANTS + ID, expectedId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete restaurant unavailable for user.")
    @WithMockUser
    public void testDeleteByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        mockMvc.perform(delete(RESTAURANTS + ID, expectedId))
                .andExpect(status().isForbidden());
    }

}
