package ru.denisovmaksim.voting;

import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.denisovmaksim.voting.model.Role;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.repository.UserRepository;

@Profile("dev")
@Component
@AllArgsConstructor
public class SampleDataLoader implements ApplicationRunner {
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        userRepository.save(new User(1L, "admin@mail.com", "admin", Role.ADMIN));
        userRepository.save(new User(2L, "user@mail.com", "user", Role.USER));
    }
}
