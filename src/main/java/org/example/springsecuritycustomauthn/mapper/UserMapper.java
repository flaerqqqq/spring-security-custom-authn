package org.example.springsecuritycustomauthn.mapper;

import org.example.springsecuritycustomauthn.config.GlobalMapperConfig;
import org.example.springsecuritycustomauthn.dto.user.*;
import org.example.springsecuritycustomauthn.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = GlobalMapperConfig.class)
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(UserRegisterDto registerDto);

    UserDetailsDto toDetailsDto(User user);

    UserRegisterDto toRegisterDto(UserRegisterRequestDto registerRequestDto);

    UserDetailsResponseDto toDetailsResponseDto(UserDetailsDto detailsDto);

    UserUsernamePasswordLoginDto toUsernamePasswordLoginDto(
            UserUsernamePasswordLoginRequestDto userUsernamePasswordLoginRequestDto
    );
}