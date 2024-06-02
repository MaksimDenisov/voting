package ru.denisovmaksim.voting.rest.admin;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.utils.JsonUtils;
import ru.denisovmaksim.voting.utils.TestData;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.rest.RestaurantsController.ID;
import static ru.denisovmaksim.voting.rest.admin.AdminRestaurantsController.ADMIN_RESTAURANTS;
import static ru.denisovmaksim.voting.utils.JsonUtils.fromJson;

@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest()
class AdminRestaurantsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantsRepository repository;
    @BeforeEach
    public void setUp() throws IOException {
        repository.saveAll(TestData.getRestaurants());
    }
    @Test
    @DisplayName("Update restaurant for user is forbidden.")
    @WithMockUser
    public void testUpdateByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");

        mockMvc.perform(put(ADMIN_RESTAURANTS + ID, expectedId)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testDelete() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        mockMvc.perform(delete(ADMIN_RESTAURANTS + ID, expectedId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Create restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testCreate() throws Exception {
        final Restaurant expected = new Restaurant();
        expected.setName("New restaurant");
        final var response = mockMvc.perform(post(ADMIN_RESTAURANTS)
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

        mockMvc.perform(post(ADMIN_RESTAURANTS)
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

        mockMvc.perform(post(ADMIN_RESTAURANTS)
                        .content(JsonUtils.asJson(expected))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Delete restaurant unavailable for user.")
    @WithMockUser
    public void testDeleteByUser() throws Exception {
        long expectedId = repository.findAll().get(0).getId();

        mockMvc.perform(delete(ADMIN_RESTAURANTS + ID, expectedId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update restaurant available for admins.")
    @WithMockUser(roles = {"ADMIN"})
    public void testUpdate() throws Exception {
        long expectedId = repository.findAll().get(0).getId();
        Restaurant expected = new Restaurant(expectedId, "Updated name");
        final var response = mockMvc.perform(put(ADMIN_RESTAURANTS + ID, expectedId)
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
}
