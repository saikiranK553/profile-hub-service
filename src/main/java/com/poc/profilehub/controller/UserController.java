package com.poc.profilehub.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UserAuthDto;
import com.poc.profilehub.dto.UserResponseDto;
import com.poc.profilehub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService=userService;
	}
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> registerUser(@RequestBody @Valid RegisterUserDto registerUserDto){
		return ResponseEntity.ok().body(userService.createUser(registerUserDto));
	}
	
	//@GetMapping
	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "10") int size) {
	    Page<UserResponseDto> usersPage = userService.getAllUsers(page,size);
	    return ResponseEntity.ok(usersPage);
	}
	
	//@GetMapping("/{id}")
	@GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	//@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
		System.out.println("inside the view");
	    UserResponseDto user = userService.getUserById(id);
	    return ResponseEntity.ok(user);
	}
	
	//@DeleteMapping("/{id}")
	@DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
	    String message = userService.deleteUserById(id);
	    return ResponseEntity.ok(message);  // HTTP 200 OK with message in body
	}
	
	//@GetMapping("/auth")
	@GetMapping("/auth")
    //@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<UserAuthDto> getUserForAuth(@RequestParam String email) {
	    return ResponseEntity.ok(userService.getUserAuthByEmail(email));
	}




}
