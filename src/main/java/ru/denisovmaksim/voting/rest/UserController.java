package ru.denisovmaksim.voting.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.model.User;

@RestController
@RequestMapping("${base-url}")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);
    public static final String PROFILE = "/profile";

    @GetMapping(PROFILE)
    public User getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Get profile: User {} has authorities: {}",
                userDetails.getUsername(), userDetails.getAuthorities());
        User user = new User();
        user.setEmail(userDetails.getUsername());
        return user;
    }
}
