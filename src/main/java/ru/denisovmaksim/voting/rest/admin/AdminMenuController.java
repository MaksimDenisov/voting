package ru.denisovmaksim.voting.rest.admin;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("${base-url}" + AdminMenuController.MENUS)
@Slf4j
@RestController
@Tag(name = "Restaurants available for admins.")
public class AdminMenuController {
    public static final String MENUS = "/admin/restaurants/{id}/menus";
}
