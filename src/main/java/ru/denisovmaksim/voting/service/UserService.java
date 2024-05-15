package ru.denisovmaksim.voting.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.repository.UserRepository;

import java.util.List;

@Service
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return buildSpringUser(repository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not found user with 'email': " + username)));
    }

    private UserDetails buildSpringUser(final User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(user.getRole())
        );
    }
}
