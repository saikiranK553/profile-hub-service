package com.poc.profilehub.service;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UserAuthDto;
import com.poc.profilehub.dto.UserResponseDto;
import com.poc.profilehub.exception.UserAlreadyExistsException;
import com.poc.profilehub.exception.UserNotFoundException;
import com.poc.profilehub.mapper.UserMapper;
import com.poc.profilehub.model.User;
import com.poc.profilehub.repository.UserRepository;
@Service
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserMapper userMapper;
	
	public UserServiceImpl(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder,UserMapper userMapper) {
		this.userRepository=userRepository;
		this.bCryptPasswordEncoder=bCryptPasswordEncoder;
		this.userMapper=userMapper;
	}

	@Override
	public UserResponseDto createUser(RegisterUserDto registerUserDto) {
		if (userRepository.findByEmail(registerUserDto.getEmail()).isPresent()) {
	        throw new UserAlreadyExistsException("User already exists with email: " + registerUserDto.getEmail());
	    }
		User user = userMapper.toEntity(registerUserDto);
		user.setPassword(bCryptPasswordEncoder.encode(registerUserDto.getPassword()));
		user.setCreatedAt(LocalDateTime.now()); 
		User savedUser = userRepository.save(user);
		return userMapper.toResponseDto(savedUser);
	}

	@Override
	public Page<UserResponseDto> getAllUsers(int page,int size) {
		Pageable pageable = PageRequest.of(page, size);
		return userRepository.findAll(pageable)
                .map(userMapper::toResponseDto);
	}
	
	@Override
	public UserResponseDto getUserById(Long id) {
	    User user = userRepository.findById(id)
	        .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	    return userMapper.toResponseDto(user);
	}
	
	@Override
	public String deleteUserById(Long id) {
	    User user = userRepository.findById(id)
	        .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
	    userRepository.delete(user);
	    return "User with id " + id + " deleted successfully";
	}

	@Override
	public UserAuthDto getUserAuthByEmail(String email) {
	    User user = userRepository.findByEmail(email)
	        .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	    return userMapper.toUserAuthDto(user);
	}




}
