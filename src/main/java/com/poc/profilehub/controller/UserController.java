package com.poc.profilehub.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UpdateUserRoleDto;
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
	
	
	@GetMapping("/admin/list")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Page<UserResponseDto>> getAllUsers(@RequestParam(defaultValue = "0") int page,
		    @RequestParam(defaultValue = "10") int size) {
	    Page<UserResponseDto> usersPage = userService.getAllUsers(page,size);
	    return ResponseEntity.ok(usersPage);
	}
	
	@GetMapping("/view/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
		System.out.println("inside the view");
	    UserResponseDto user = userService.getUserById(id);
	    return ResponseEntity.ok(user);
	}
	
	@DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
	    String message = userService.deleteUserById(id);
	    Map<String, String> response = new HashMap<>();
	    response.put("message", message);
	    response.put("success", "true");
	    return ResponseEntity.ok(response);

	}
	
	@GetMapping("/auth")
	public ResponseEntity<UserAuthDto> getUserForAuth(@RequestParam String email) {
	    return ResponseEntity.ok(userService.getUserAuthByEmail(email));
	}
	
	@PatchMapping("/admin/update-role/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateUserRole(@PathVariable Long id, 
            @RequestBody @Valid UpdateUserRoleDto updateRoleDto) {
        try {
            String message=userService.updateUserRole(id, updateRoleDto.getRole());
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", message);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Failed to update user role: " + e.getMessage());
            response.put("success", false);
            
            return ResponseEntity.badRequest().body(response);
        }
    }




}
