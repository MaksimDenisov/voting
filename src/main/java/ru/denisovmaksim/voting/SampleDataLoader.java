package ru.denisovmaksim.voting;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.model.Role;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.repository.UserRepository;

@Profile("dev")
@Component
@AllArgsConstructor
public class SampleDataLoader implements ApplicationRunner {
    private final UserRepository userRepository;
    private final RestaurantsRepository restaurantsRepository;
    @Override
    public void run(ApplicationArguments args) {
        userRepository.deleteAll();
        restaurantsRepository.deleteAll();

        userRepository.save(new User(1L, "admin@mail.com",
                new BCryptPasswordEncoder().encode("admin"), Role.ADMIN));
        userRepository.save(new User(2L, "user@mail.com",
                new BCryptPasswordEncoder().encode("user"), Role.USER));

        restaurantsRepository.save(new Restaurant(null, "Meat restaurant"));
        restaurantsRepository.save(new Restaurant(null, "Seafood restaurant"));
    }
}
