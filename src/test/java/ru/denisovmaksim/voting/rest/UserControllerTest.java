package ru.denisovmaksim.voting.rest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.denisovmaksim.voting.config.SpringConfigForIT;
import ru.denisovmaksim.voting.utils.TestUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class UserControllerTest {

    @Autowired
    private TestUtils utils;

    @Value("${base-url}")
    private String baseUrl;
    @Test
    @DisplayName("Get profile for authorized user.")
    void getProfileByUser() throws Exception {
        utils.perform(get(baseUrl +  UserController.PROFILE)
                .with(utils.getUser()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Get profile for authorized admin.")
    void getProfileByAdmin() throws Exception {
        utils.perform(get(baseUrl +  UserController.PROFILE)
                        .with(utils.getAdmin()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Unauthorized user should return 401")
    void getProfileByUnauthorizedUser() throws Exception {
        utils.perform(get(UserController.PROFILE))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();
    }
}
