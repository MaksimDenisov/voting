package ru.denisovmaksim.voting.rest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${base-url}")
public class UserController {

    public static final String PROFILE = "/profile";

    @GetMapping(PROFILE)
    public UserDetails getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.printf("User %s has authorities: %s", userDetails.getUsername(), userDetails.getAuthorities());
        return userDetails;
    }
}
