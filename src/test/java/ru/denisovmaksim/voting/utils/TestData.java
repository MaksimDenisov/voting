package ru.denisovmaksim.voting.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.model.Role;
import ru.denisovmaksim.voting.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public class TestData {
    private static final String RESOURCES = "src/test/resources/";

    private static final String ADMIN_PASS = "admin";
    private static final String USER_PASS = "user";
    public static final User ADMIN = new User(1L, "admin@mail.com",
            new BCryptPasswordEncoder().encode(ADMIN_PASS), Role.ADMIN);
    public static final User USER = new User(2L, "user@mail.com",
            new BCryptPasswordEncoder().encode(USER_PASS), Role.USER);

    public static final RequestPostProcessor ADMIN_BASIC_AUTH =  httpBasic(ADMIN.getEmail(), ADMIN_PASS);
    public static final RequestPostProcessor USER_BASIC_AUTH =  httpBasic(USER.getEmail(), USER_PASS);

    public static List<Restaurant> getRestaurants() throws IOException {
        return JsonUtils.fromJson(
                Files.readString(getExpectedPath("restaurants.json")),
                new TypeReference<>() {
                });
    }

    private static Path getExpectedPath(String filename) {
        return Paths.get(RESOURCES + "fixtures/" + filename)
                .toAbsolutePath()
                .normalize();
    }
}
