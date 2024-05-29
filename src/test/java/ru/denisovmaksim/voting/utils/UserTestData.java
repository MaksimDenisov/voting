package ru.denisovmaksim.voting.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.denisovmaksim.voting.model.Role;
import ru.denisovmaksim.voting.model.User;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

public class UserTestData {
    private static final String ADMIN_PASS = "admin";
    private static final String USER_PASS = "user";
    public static final User ADMIN = new User(1L, "admin@mail.com",
            new BCryptPasswordEncoder().encode(ADMIN_PASS), Role.ADMIN);
    public static final User USER = new User(2L, "user@mail.com",
            new BCryptPasswordEncoder().encode(USER_PASS), Role.USER);

    public static final RequestPostProcessor ADMIN_BASIC_AUTH =  httpBasic(ADMIN.getEmail(), ADMIN_PASS);
    public static final RequestPostProcessor USER_BASIC_AUTH =  httpBasic(USER.getEmail(), USER_PASS);
}
