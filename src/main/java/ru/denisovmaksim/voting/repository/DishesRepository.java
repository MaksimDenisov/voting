package ru.denisovmaksim.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.denisovmaksim.voting.model.Dish;

import java.util.List;

@Repository
public interface DishesRepository extends JpaRepository<Dish, Long> {

    @Query("SELECT d FROM Dish d JOIN FETCH d.restaurant r")
    List<Dish> getAllWithRestaurants();
}

