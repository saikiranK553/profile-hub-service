package com.poc.profilehub.mapper;

import org.mapstruct.Mapper;

import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UserAuthDto;
import com.poc.profilehub.dto.UserResponseDto;
import com.poc.profilehub.model.User;
@Mapper(componentModel = "spring")
public interface UserMapper {
	User toEntity(RegisterUserDto dto);
	
	UserResponseDto toResponseDto(User user);
	
    UserAuthDto toUserAuthDto(User user);
}
