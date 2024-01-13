package ru.denisovmaksim.voting.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.denisovmaksim.voting.config.SpringConfigForIT;
import ru.denisovmaksim.voting.model.Restaurant;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.service.RestaurantsService;
import ru.denisovmaksim.voting.utils.TestData;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles(SpringConfigForIT.TEST_PROFILE)
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringConfigForIT.class)
public class RestaurantsControllerTest {

    @Autowired
    private TestData testData;

    @Autowired
    private RestaurantsRepository repository;

    @Autowired
    private RestaurantsService service;

    @BeforeEach
    public void setUp() throws IOException {
        repository.saveAll(testData.getRestaurants());
    }

    @AfterEach
    public void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Get all restaurants.")
    public void testGetAll() throws Exception {
        List<Restaurant> expected = testData.getRestaurants();
        List<Restaurant> actual = service.getAll();

        assertThat(actual)
                .usingRecursiveFieldByFieldElementComparator()
                .containsAll(expected);
    }
}
