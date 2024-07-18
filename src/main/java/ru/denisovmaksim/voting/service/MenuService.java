package ru.denisovmaksim.voting.service;

import org.springframework.stereotype.Service;
import ru.denisovmaksim.voting.dto.MenuDTO;

import java.time.LocalDate;
import java.util.List;

@Service
public class MenuService {

    public MenuDTO getAllMenuByDate(LocalDate date) {
        return null;
    }

    public MenuDTO getOneMenuByDate(Long restaurantId, LocalDate date) {
        return null;
    }

    public MenuDTO addCurrentMenu(Long restaurantId, List<Long> dishIds) {
        return null;
    }

    public MenuDTO updateCurrentMenu(Long restaurantId, List<Long> dishIds) {
        return null;
    }

    public boolean deleteCurrentMenu(Long restaurantId) {
        return false;
    }
}
