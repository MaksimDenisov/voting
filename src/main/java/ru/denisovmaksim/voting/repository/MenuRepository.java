package ru.denisovmaksim.voting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.denisovmaksim.voting.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
}
