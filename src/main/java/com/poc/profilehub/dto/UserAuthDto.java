package com.poc.profilehub.dto;

import com.poc.profilehub.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDto {
	private Long id;
    private String email;
    private String password;
    private Role role;
    private String username;
}
