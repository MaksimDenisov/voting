package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
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
import ru.denisovmaksim.voting.dto.UserCreationDTO;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.repository.UserRepository;
import ru.denisovmaksim.voting.utils.TestUtils;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.denisovmaksim.voting.utils.TestUtils.ADMIN;
import static ru.denisovmaksim.voting.utils.TestUtils.ADMIN_BASIC_AUTH;
import static ru.denisovmaksim.voting.utils.TestUtils.USER;
import static ru.denisovmaksim.voting.utils.TestUtils.USER_BASIC_AUTH;

@AutoConfigureMockMvc
@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = SpringConfigForIT.class)
class UserControllerTest {

    @Value("${base-url}")
    private String baseUrl;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(ADMIN);
        userRepository.save(USER);
    }

    @Test
    @DisplayName("Get profile for authorized user.")
    void getProfileByUser() throws Exception {
        final var response = mockMvc.perform(get(baseUrl + UserController.PROFILE)
                        .with(USER_BASIC_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final User actual = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        final User expected = userRepository.findByEmail(USER.getEmail()).orElseThrow();
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Get profile for authorized admin.")
    void getProfileByAdmin() throws Exception {
        final var response = mockMvc.perform(get(baseUrl + UserController.PROFILE)
                        .with(ADMIN_BASIC_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User expected = userRepository.findByEmail(ADMIN.getEmail()).orElseThrow();
        final User actual = TestUtils.fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Create user should return user.")
    void createUser() throws Exception {
        UserCreationDTO newUser = new UserCreationDTO("new-user@mail.com", "new_user_pass");
        mockMvc.perform(post(baseUrl + UserController.SIGNUP)
                .content(TestUtils.asJson(newUser))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/profile")))
                .andReturn()
                .getResponse();
        final User actual = userRepository.findByEmail(newUser.getEmail()).orElseThrow();
        assertEquals(newUser.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Create duplicated  user should return bad request.")
    void createDuplicatedUser() throws Exception {
        mockMvc.perform(post(baseUrl + UserController.SIGNUP)
                .content(TestUtils.asJson(new UserCreationDTO(USER.getEmail(), USER.getPassword())))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Unauthorized user should return 401")
    void getProfileByUnauthorizedUser() throws Exception {
        mockMvc.perform(get(UserController.PROFILE))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse();
    }
}
