package com.poc.profilehub.dto;

import com.poc.profilehub.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPrincipal {
	private String email;
    private Long userId;
    private Role role;
}
