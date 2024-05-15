package ru.denisovmaksim.voting.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;
import ru.denisovmaksim.voting.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class TestData {
    private static final String RESOURCES = "src/test/resources/";

    public List<User> getUsers() throws IOException {
        return TestUtils.fromJson(
                Files.readString(getExpectedPath("users.json")),
                new TypeReference<>() {
                });
    }

    private static Path getExpectedPath(String filename) {
        return Paths.get(RESOURCES + "fixtures/" + filename)
                .toAbsolutePath()
                .normalize();
    }
}
