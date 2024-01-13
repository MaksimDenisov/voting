package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.denisovmaksim.voting.config.SpringConfigForIT;
import ru.denisovmaksim.voting.dto.RestaurantTO;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.repository.UserRepository;
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
import static ru.denisovmaksim.voting.utils.TestData.ADMIN;
import static ru.denisovmaksim.voting.utils.TestData.ADMIN_BASIC_AUTH;
import static ru.denisovmaksim.voting.utils.TestData.USER;
import static ru.denisovmaksim.voting.utils.TestData.USER_BASIC_AUTH;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringConfigForIT.class)
public class RestaurantsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RestaurantsRepository repository;

    @BeforeEach
    public void setUp() throws IOException {
        userRepository.deleteAll();
        userRepository.save(ADMIN);
        userRepository.save(USER);
        repository.saveAll(TestData.getRestaurants());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get all restaurants available for authorized users.")
    public void testGetAll() throws Exception {
        final var response = mockMvc.perform(get(RESTAURANTS)
                        .with(USER_BASIC_AUTH))
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
    public void testGetAllByUnauthorized() throws Exception {
        mockMvc.perform(get(RESTAURANTS))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get one restaurant available for authorized users.")
    public void testGetOne() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        final var response = mockMvc.perform(
                        get(RESTAURANTS + ID, expected.getId())
                                .with(USER_BASIC_AUTH))
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
    public void testGetOneByUnauthorized() throws Exception {
        final Restaurant expected = repository.findAll().get(0);
        mockMvc.perform(get(RESTAURANTS + ID, expected.getId()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Create restaurant available for admins.")
    public void testCreate() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        final var response = mockMvc.perform(post(RESTAURANTS)
                        .with(ADMIN_BASIC_AUTH)
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
    public void testCreateInvalid() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("");
        mockMvc.perform(post(RESTAURANTS)
                        .with(ADMIN_BASIC_AUTH)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Create restaurant for user is forbidden.")
    public void testCreateByUser() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        mockMvc.perform(post(RESTAURANTS)
                        .with(USER_BASIC_AUTH)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update restaurant available for admins.")
    public void testUpdate() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        final var response = mockMvc.perform(put(RESTAURANTS + ID, expectedId)
                        .with(ADMIN_BASIC_AUTH)
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
    public void testUpdateByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        mockMvc.perform(put(RESTAURANTS + ID, expectedId)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON)
                        .with(USER_BASIC_AUTH))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete restaurant available for admins.")
    public void testDelete() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        mockMvc.perform(delete(RESTAURANTS + ID, expectedId)
                        .with(ADMIN_BASIC_AUTH))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Delete restaurant unavailable for user.")
    public void testDeleteByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        mockMvc.perform(delete(RESTAURANTS + ID, expectedId)
                        .with(USER_BASIC_AUTH))
                .andExpect(status().isForbidden());
    }

}
