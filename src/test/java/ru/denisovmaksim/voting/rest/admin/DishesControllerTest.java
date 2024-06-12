package ru.denisovmaksim.voting.rest.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.model.Dish;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.rest.AbstractMockMvcTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static ru.denisovmaksim.voting.rest.admin.DishesController.DISHES_PATH;
import static ru.denisovmaksim.voting.rest.admin.DishesController.ID;

class DishesControllerTest extends AbstractMockMvcTest {
    private static final Long MEAT_RESTAURANT_ID = 1L;

    @Autowired
    private DishesRepository dishesRepository;

    @Autowired
    private RestaurantsRepository restaurantsRepository;

    @Test
    @DisplayName("Admin: get all restaurants with dishes.")
    @WithMockUser(roles = "ADMIN")
    public void getAllByRestaurantId() {
        System.out.println("===========================================================");
        System.out.println("===========================================================");
        System.out.println("===========================================================");
        List<Dish> dishes = dishesRepository.getAllWithRestaurants();
        dishes.forEach(System.out::println);
        System.out.println("===========================================================");
        System.out.println("===========================================================");
        System.out.println("===========================================================");
        fail();
    }

    @Test
    @DisplayName("Admin: Get one restaurant with dishes.")
    @WithMockUser(roles = "ADMIN")
    public void getById() {
        fail();
    }

    @Test
    @DisplayName("Admin: Create dish for restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void create() {
        fail();
    }

    @Test
    @DisplayName("Admin: Update dish for restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void update() {
        fail();
    }

    @Test
    @DisplayName("Admin: Delete dish from restaurant.")
    @WithMockUser(roles = "ADMIN")
    public void testDelete() {
        fail();
    }

    @Test
    @DisplayName("User: Get all restaurants with dishes is forbidden.")
    @WithMockUser
    public void getAllByRestaurantIdByUser() throws Exception {
        checkRequestIsForbidden(get(DISHES_PATH, MEAT_RESTAURANT_ID));
    }

    @Test
    @DisplayName("User: Get one restaurant with dishes is forbidden.")
    @WithMockUser
    public void getByIdByUser() throws Exception {
        Dish dish = dishesRepository.findAll().get(0);
        checkRequestIsForbidden(get(DISHES_PATH + ID, MEAT_RESTAURANT_ID, dish.getId()));
    }

    @Test
    @DisplayName("User: Create dish forbidden.")
    @WithMockUser
    public void createByUser() throws Exception {
        checkRequestIsForbidden(post(DISHES_PATH, MEAT_RESTAURANT_ID)
                .content(asJson(getNewDish()))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("User: Update dish forbidden.")
    @WithMockUser
    public void updateByUser() throws Exception {
        Dish updatedDish = getUpdatedDish();
        checkRequestIsForbidden(put(DISHES_PATH + ID, MEAT_RESTAURANT_ID, updatedDish.getId())
                .content(asJson(updatedDish))
                .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("User: Delete dish forbidden.")
    @WithMockUser
    public void deleteByUser() throws Exception {
        Dish dish = dishesRepository.findAll().get(0);
        checkRequestIsForbidden(delete(DISHES_PATH + ID, MEAT_RESTAURANT_ID, dish.getId()));
    }

    private Dish getNewDish() {
        return new Dish(null, "New dish", 10000, restaurantsRepository.getReferenceById(MEAT_RESTAURANT_ID));
    }

    private Dish getUpdatedDish() {
        Dish dish = dishesRepository.findAll().get(0);
        dish.setName("Updated dish");
        return dish;
    }
}
