package com.poc.profilehub.dto;

import java.time.LocalDateTime;

import com.poc.profilehub.model.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterUserDto {
	@NotBlank(message = "Username is required")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
	private String username;
	@NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
	@NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String phoneNumber;
    private String address;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
}
