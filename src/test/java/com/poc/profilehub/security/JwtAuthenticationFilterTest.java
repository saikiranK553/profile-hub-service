package com.poc.profilehub.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.profilehub.model.Role;
import com.poc.profilehub.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

class JwtAuthenticationFilterTest {

	private JwtAuthenticationFilter filter;
    private JwtUtils jwtUtils;
    private ObjectMapper objectMapper;

    private final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsInRva2VuVHlwZSI6IkFDQ0VTUyIsInVzZXJJZCI6NSwic3ViIjoic2Fpa2lyYW4xMkBnbWFpbC5jb20iLCJpYXQiOjE3NTM3Njc1MjgsImV4cCI6MTc1Mzc2ODQyOH0.f7hZtXEbFQFI0cjLcIqvoHzmWGaeIGI016Yev39KKTo/H6nev8kM=";

    @BeforeEach
    void setUp() {
        jwtUtils = mock(JwtUtils.class);
        objectMapper = new ObjectMapper();

        filter = new JwtAuthenticationFilter();
        ReflectionTestUtils.setField(filter, "jwtUtil", jwtUtils);
        ReflectionTestUtils.setField(filter, "objectMapper", objectMapper);

        SecurityContextHolder.clearContext();
    }
    
    @Test
    void shouldSkipFilterWhenNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin/list");

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
    
    @Test
    void shouldSetAuthenticationOnValidToken() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/admin/list");
        request.addHeader("Authorization", "Bearer " + TEST_TOKEN);

        when(jwtUtils.extractEmail(TEST_TOKEN)).thenReturn("saikiran@gmail.com");
        when(jwtUtils.isTokenValid(TEST_TOKEN, "saikiran@gmail.com")).thenReturn(true);
        when(jwtUtils.extractRole(TEST_TOKEN)).thenReturn(Role.USER);
        when(jwtUtils.extractUserId(TEST_TOKEN)).thenReturn(1L);

        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals("saikiran@gmail.com",
                ((com.poc.profilehub.dto.UserPrincipal) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal()).getEmail());
        verify(filterChain).doFilter(request, response);
    }

}
