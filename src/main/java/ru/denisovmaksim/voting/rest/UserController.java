package ru.denisovmaksim.voting.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.denisovmaksim.voting.dto.UserCreationDTO;
import ru.denisovmaksim.voting.dto.UserDTO;
import ru.denisovmaksim.voting.model.User;
import ru.denisovmaksim.voting.service.UserService;

import java.net.URI;

@RestController
@RequestMapping("${base-url}")
@Tag(name = "Profiles")
public class UserController {
    public static final String PROFILE = "/profile";
    public static final String SIGNUP = "/signup";
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(PROFILE)
    @Operation(summary = "Getting my profile.")
    @ApiResponses(@ApiResponse(responseCode = "200", content =
    @Content(schema = @Schema(implementation = UserDTO.class))
    ))
    public UserDTO getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        logger.info("Get profile: User {} has authorities: {}",
                userDetails.getUsername(), userDetails.getAuthorities());
        return new UserDTO(userDetails.getUsername());
    }

    @Operation(summary = "SignUp.")
    @ApiResponses(@ApiResponse(responseCode = "201", content =
    @Content(schema = @Schema(implementation = UserDTO.class))
    ))
    @PostMapping(SIGNUP)
    public ResponseEntity<UserDTO> create(@RequestBody UserCreationDTO userCreationDTO) {
        User user = service.create(userCreationDTO);
        logger.info("Create user with name {}", user.getEmail());
        URI location = ServletUriComponentsBuilder.fromCurrentServletMapping()
                .path(PROFILE)
                .build().toUri();
        return ResponseEntity.created(location)
                .body(new UserDTO(user.getEmail()));
    }
}
