package com.poc.profilehub.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UserAuthDto;
import com.poc.profilehub.dto.UserResponseDto;
import com.poc.profilehub.model.Role;

public interface UserService {
	UserResponseDto createUser(RegisterUserDto registerUserDto);
	Page<UserResponseDto> getAllUsers(int page,int size);
	UserResponseDto getUserById(Long id);
	String deleteUserById(Long id);
	UserAuthDto getUserAuthByEmail(String email);
	String updateUserRole(Long userId, Role newRole);
}
