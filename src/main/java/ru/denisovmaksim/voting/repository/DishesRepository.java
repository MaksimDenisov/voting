package ru.denisovmaksim.voting.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.denisovmaksim.voting.model.Dish;

import java.util.List;

@Repository
public interface DishesRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r WHERE d.restaurant.id =:restaurantId")
    List<Dish> getAllWithRestaurantsByRestaurantId(Long restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id =:restaurantId")
    List<Dish> getAllByRestaurantId(Long restaurantId);

    @Query("SELECT d FROM Dish d WHERE d.restaurant.id =:restaurantId AND d.id = :dishId")
    Dish getById(Long restaurantId, Long dishId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Dish d WHERE d.restaurant.id =:restaurantId AND d.id = :dishId")
    void delete(Long restaurantId, Long dishId);
}

