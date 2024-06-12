package ru.denisovmaksim.voting.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.denisovmaksim.voting.dto.UserCreationDTO;
import ru.denisovmaksim.voting.model.Role;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.repository.UserRepository;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractMockMvcTest {
    private static final String ADMIN_PASS = "admin";
    private static final String USER_PASS = "user";
    public static final User ADMIN = new User(1L, "admin@mail.com",
            new BCryptPasswordEncoder().encode(ADMIN_PASS), Role.ADMIN);
    public static final User USER = new User(2L, "user@mail.com",
            new BCryptPasswordEncoder().encode(USER_PASS), Role.USER);

    public static final RequestPostProcessor ADMIN_BASIC_AUTH =  httpBasic(ADMIN.getEmail(), ADMIN_PASS);
    public static final RequestPostProcessor USER_BASIC_AUTH =  httpBasic(USER.getEmail(), USER_PASS);

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
        final var response = perform(get(UserController.PROFILE)
                        .with(USER_BASIC_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        final User actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });

        final User expected = userRepository.findByEmail(USER.getEmail()).orElseThrow();
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Get profile for authorized admin.")
    void getProfileByAdmin() throws Exception {
        final var response = perform(get(UserController.PROFILE)
                        .with(ADMIN_BASIC_AUTH))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        final User expected = userRepository.findByEmail(ADMIN.getEmail()).orElseThrow();
        final User actual = fromJson(response.getContentAsString(), new TypeReference<>() {
        });
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Create user should return user.")
    void createUser() throws Exception {
        UserCreationDTO newUser = new UserCreationDTO("new-user@mail.com", "new_user_pass");
        perform(post(UserController.SIGNUP)
                        .content(asJson(newUser))
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
        perform(post(UserController.SIGNUP)
                        .content(asJson(new UserCreationDTO(USER.getEmail(), USER.getPassword())))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse();
    }

    @Test
    @DisplayName("Unauthorized user should return 401")
    void getProfileByUnauthorizedUser() throws Exception {
        perform(get(UserController.PROFILE))
                .andExpect(status().isUnauthorized());
    }
}
