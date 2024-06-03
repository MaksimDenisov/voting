package ru.denisovmaksim.voting.rest.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import ru.denisovmaksim.voting.model.Dish;
import ru.denisovmaksim.voting.repository.DishesRepository;
import ru.denisovmaksim.voting.repository.RestaurantsRepository;
import ru.denisovmaksim.voting.rest.AbstractMockMvcTest;

import java.util.List;

class DishesControllerTest extends AbstractMockMvcTest {


    @Autowired
    private DishesRepository dishesRepository;

    @Test
    @DisplayName("Get all restaurants with dishes available for admins.")
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

    }

    @Test
    @DisplayName("Get all restaurants with dishes available for admins.")
    @WithMockUser(roles = "ADMIN")
    public void getById() {
    }

    @Test
    @DisplayName("Create dish for restaurant available for admins.")
    @WithMockUser(roles = "ADMIN")
    public void create() {
    }

    @Test
    @DisplayName("Update dish for restaurant available for admins.")
    @WithMockUser(roles = "ADMIN")
    public void update() {
    }

    @Test
    @DisplayName("Delete dish from restaurant available for admins.")
    @WithMockUser(roles = "ADMIN")
    public void delete() {
    }

    @Test
    @DisplayName("Get all restaurants with dishes unavailable for users.")
    @WithMockUser
    public void getAllByRestaurantIdByUser() {
    }

    @Test
    @DisplayName("Get all restaurants with dishes unavailable for users.")
    @WithMockUser
    public void getByIdByUser() {
    }

    @Test
    @DisplayName("Create dish for restaurant unavailable for users.")
    @WithMockUser
    public void createByUser() {
    }

    @Test
    @DisplayName("Update dish for restaurant available for admins.")
    @WithMockUser
    public void updateByUser() {
    }

    @Test
    @DisplayName("Delete dish from restaurant available for admins.")
    @WithMockUser
    public void deleteByUser() {
    }

}
