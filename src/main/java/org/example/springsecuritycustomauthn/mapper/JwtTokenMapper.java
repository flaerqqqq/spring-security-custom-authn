package org.example.springsecuritycustomauthn.mapper;

import org.example.springsecuritycustomauthn.config.GlobalMapperConfig;
import org.example.springsecuritycustomauthn.dto.auth.JwtTokenDto;
import org.example.springsecuritycustomauthn.dto.auth.JwtTokenResponseDto;
import org.mapstruct.Mapper;

@Mapper(config = GlobalMapperConfig.class)
public interface JwtTokenMapper {

    JwtTokenResponseDto toResponseDto(JwtTokenDto dto);
}