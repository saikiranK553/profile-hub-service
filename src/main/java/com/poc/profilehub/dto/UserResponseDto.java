package com.poc.profilehub.dto;

import java.time.LocalDateTime;

import com.poc.profilehub.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserResponseDto {
	private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    private String phoneNumber;
    private String address;
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private String profileImageUrl;
}
