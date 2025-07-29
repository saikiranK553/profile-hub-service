package com.poc.profilehub.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.profilehub.dto.RegisterUserDto;
import com.poc.profilehub.dto.UserResponseDto;
import com.poc.profilehub.model.Role;
import com.poc.profilehub.service.UserService;
import com.poc.profilehub.utils.JwtUtils;
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;
	@MockBean
	private UserService userService;
	
	@MockBean
	private JwtUtils jwtUtils;

	@Test
	void testRegisterUser() throws Exception{
		RegisterUserDto registerUserDto=new RegisterUserDto(
                "saikiran53", "password", "saikiran@gmail.com",
                "saikiran", "kiran", Role.USER, "9988998899", "colony", "A bio",
                LocalDateTime.now(), LocalDateTime.now()
        );
		
		UserResponseDto responseDto = new UserResponseDto(
                1L, "saikiran53", "saikiran@gmail.com",
                "saikiran", "kiran", Role.USER, "9988998899", "colony", "A bio",
                LocalDateTime.now(), LocalDateTime.now(), null
        );
		
		Mockito.when(userService.createUser(registerUserDto)).thenReturn(responseDto);
		
		mockMvc.perform(post("/api/users/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(registerUserDto)))
				.andExpect(status().isOk());
	}
	
	
	@Test
	@WithMockUser(roles = "ADMIN")  
	void testGetAllUsersAsAdmin() throws Exception {
		UserResponseDto user1 = new UserResponseDto(
                1L, "saikiran5", "saikiran@gmail.com",
                "saikiran", "kiran", Role.USER, "9988998899", "colony", "A bio",
                LocalDateTime.now(), LocalDateTime.now(), null
        );
		UserResponseDto user2 = new UserResponseDto(
                1L, "saikiran53", "saikiran@gmail.com",
                "saikiran", "kiran", Role.USER, "9988998899", "colony", "A bio",
                LocalDateTime.now(), LocalDateTime.now(), null
        );
	    
	    Page<UserResponseDto> page = new PageImpl<>(List.of(user1, user2));
	    
	    when(userService.getAllUsers(0, 10)).thenReturn(page);
	    
	    mockMvc.perform(get("/api/users/admin/list")
	                    .param("page", "0")
	                    .param("size", "10"))
	            .andExpect(status().isOk())
	            .andExpect(jsonPath("$.content").isArray())
	            .andExpect(jsonPath("$.content.length()").value(2))
	            .andExpect(jsonPath("$.content[0].username").value("saikiran5"))
	            .andExpect(jsonPath("$.content[1].username").value("saikiran53"));
	    
	    verify(userService, times(1)).getAllUsers(0, 10);
	}

	
	

}
