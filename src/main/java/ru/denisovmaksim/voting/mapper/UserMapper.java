package ru.denisovmaksim.voting.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.core.userdetails.UserDetails;
import ru.denisovmaksim.voting.dto.UserDTO;
import ru.denisovmaksim.voting.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toUserDTO(User user);

    @Mapping(source = "username", target = "email")
    UserDTO toUserDTO(UserDetails user);
}
