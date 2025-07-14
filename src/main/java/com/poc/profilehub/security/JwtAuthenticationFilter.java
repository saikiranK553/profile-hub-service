package com.poc.profilehub.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.poc.profilehub.dto.ErrorResponse;
import com.poc.profilehub.dto.UserPrincipal;
import com.poc.profilehub.model.Role;
import com.poc.profilehub.utils.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
//	@Autowired
//    private JwtUtils jwtUtil;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, 
//                                  FilterChain filterChain) throws ServletException, IOException {
//        
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail;
//        System.out.println("inside the filter");
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//        
//        try {
//            userEmail = jwtUtil.extractEmail(jwt);
//
//            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                
//                if (jwtUtil.isTokenValid(jwt, userEmail)) {
//                    Role role = jwtUtil.extractRole(jwt);
//                    Long userId = jwtUtil.extractUserId(jwt);
//                    
//                    List<GrantedAuthority> authorities = new ArrayList<>();
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
//                    System.out.println(authorities);
//                    
//                    // Create custom user principal with user details
//                    UserPrincipal userPrincipal = new UserPrincipal(userEmail, userId, role);
//                    
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userPrincipal, null, authorities
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//            }
//        } catch (Exception e) {
//            // Token is invalid, continue without authentication
//        }
//        
//        filterChain.doFilter(request, response);
//    }
	
	
	@Autowired
    private JwtUtils jwtUtil;
	
	@Autowired
	private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        
        System.out.println("=== JWT FILTER START ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authorization Header: " + authHeader);
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No Bearer token found - skipping JWT processing");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        System.out.println("JWT Token extracted (first 50 chars): " + jwt.substring(0, Math.min(50, jwt.length())) + "...");

        try {
            userEmail = jwtUtil.extractEmail(jwt);
            System.out.println("Step 1 - Extracted email: " + userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Step 2 - Email is not null and no existing authentication");

                boolean isTokenValid = jwtUtil.isTokenValid(jwt, userEmail);
                System.out.println("Step 3 - Token valid: " + isTokenValid);

                if (isTokenValid) {
                    System.out.println("Step 4 - Processing valid token...");
                    
                    Role role = jwtUtil.extractRole(jwt);
                    System.out.println("Step 5 - Extracted role: " + role);
                    
                    Long userId = jwtUtil.extractUserId(jwt);
                    System.out.println("Step 6 - Extracted userId: " + userId);

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
                    System.out.println("Step 7 - Created authorities: " + authorities);

                    UserPrincipal userPrincipal = new UserPrincipal(userEmail, userId, role);
                    System.out.println("Step 8 - Created UserPrincipal: " + userPrincipal);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userPrincipal, null, authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    System.out.println("Step 9 - Authentication set successfully!");
                    System.out.println("Current authentication: " + SecurityContextHolder.getContext().getAuthentication());
                    System.out.println("Is authenticated: " + SecurityContextHolder.getContext().getAuthentication().isAuthenticated());
                } else {
                    System.out.println("Step 4 - Token is INVALID");
                }
            } else {
                System.out.println("Step 2 - FAILED: Email is null (" + userEmail + ") or authentication already exists (" + SecurityContextHolder.getContext().getAuthentication() + ")");
            }
        } 
//        catch (Exception e) {
//            System.out.println("EXCEPTION in JWT processing: " + e.getMessage());
//            e.printStackTrace();
//        }
        catch (ExpiredJwtException e) {
            System.out.println("JWT TOKEN EXPIRED: " + e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, 
                              "JWT_EXPIRED", "JWT token has expired", request.getRequestURI());
            return; // Don't continue the filter chain
        }catch (Exception e) {
            System.out.println("UNEXPECTED EXCEPTION in JWT processing: " + e.getMessage());
            e.printStackTrace();
            handleJwtException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "JWT_PROCESSING_ERROR", "Error processing JWT token", request.getRequestURI());
            return;
        }


        System.out.println("=== JWT FILTER END ===");
        filterChain.doFilter(request, response);
    }
    private void handleJwtException(HttpServletResponse response, int status, String error, String message, String path) {
        try {
            ErrorResponse errorResponse = new ErrorResponse(status, error, message, path);
            
            response.setStatus(status);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
            response.getWriter().write(jsonResponse);
        } catch (IOException e) {
            System.out.println("Error writing JWT exception response: " + e.getMessage());
        }
    }
}
