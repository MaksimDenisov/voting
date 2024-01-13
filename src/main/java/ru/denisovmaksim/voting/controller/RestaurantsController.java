package ru.denisovmaksim.voting.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.denisovmaksim.voting.service.RestaurantsService;

@RestController
@AllArgsConstructor
public class RestaurantsController {
    private final RestaurantsService service;
}
