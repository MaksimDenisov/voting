package ru.denisovmaksim.voting.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import ru.denisovmaksim.voting.model.Restaurant;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class TestData {
    private static final String RESOURCES = "src/test/resources/";

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
