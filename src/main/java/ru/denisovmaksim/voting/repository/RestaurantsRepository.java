package ru.denisovmaksim.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denisovmaksim.voting.model.Restaurant;

@Repository
public interface RestaurantsRepository extends JpaRepository<Restaurant, Long> {
}
