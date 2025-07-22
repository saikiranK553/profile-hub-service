package com.poc.profilehub.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	
	private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	
//	@Autowired
//    private JwtUtils jwtUtil;
//	
//	@Autowired
//	private ObjectMapper objectMapper;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
//            FilterChain filterChain) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//        final String jwt;
//        final String userEmail;
//        
//        
//        
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            System.out.println("No Bearer token found - skipping JWT processing");
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        jwt = authHeader.substring(7);
//        System.out.println("JWT Token extracted (first 50 chars): " + jwt.substring(0, Math.min(50, jwt.length())) + "...");
//
//        try {
//            userEmail = jwtUtil.extractEmail(jwt);
//
//            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//                boolean isTokenValid = jwtUtil.isTokenValid(jwt, userEmail);
//
//                if (isTokenValid) {
//                    
//                    Role role = jwtUtil.extractRole(jwt);
//                    
//                    Long userId = jwtUtil.extractUserId(jwt);
//
//                    List<GrantedAuthority> authorities = new ArrayList<>();
//                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));
//
//                    UserPrincipal userPrincipal = new UserPrincipal(userEmail, userId, role);
//
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userPrincipal, null, authorities
//                    );
//                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                    
//                    
//                } else {
//                    System.out.println("Token is INVALID");
//                }
//            } else {
//                System.out.println(" FAILED: Email is null (" + userEmail + ") or authentication already exists (" + SecurityContextHolder.getContext().getAuthentication() + ")");
//            }
//        } 
//        catch (ExpiredJwtException e) {
//            System.out.println("JWT TOKEN EXPIRED: " + e.getMessage());
//            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, 
//                              "JWT_EXPIRED", "JWT token has expired", request.getRequestURI());
//            return;
//        }catch (Exception e) {
//            System.out.println("UNEXPECTED EXCEPTION in JWT processing: " + e.getMessage());
//            e.printStackTrace();
//            handleJwtException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
//                              "JWT_PROCESSING_ERROR", "Error processing JWT token", request.getRequestURI());
//            return;
//        }
//
//
//        System.out.println("=== JWT FILTER END ===");
//        filterChain.doFilter(request, response);
//    }
//    private void handleJwtException(HttpServletResponse response, int status, String error, String message, String path) {
//        try {
//            ErrorResponse errorResponse = new ErrorResponse(status, error, message, path);
//            
//            response.setStatus(status);
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            
//            String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//            response.getWriter().write(jsonResponse);
//        } catch (IOException e) {
//            System.out.println("Error writing JWT exception response: " + e.getMessage());
//        }
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
        
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No Bearer token found - skipping JWT processing for URI: {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        logger.debug("JWT Token extracted (first 50 chars): {}...", jwt.substring(0, Math.min(50, jwt.length())));

        try {
            userEmail = jwtUtil.extractEmail(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                boolean isTokenValid = jwtUtil.isTokenValid(jwt, userEmail);

                if (isTokenValid) {
                    
                    Role role = jwtUtil.extractRole(jwt);
                    Long userId = jwtUtil.extractUserId(jwt);

                    List<GrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.name()));

                    UserPrincipal userPrincipal = new UserPrincipal(userEmail, userId, role);

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userPrincipal, null, authorities
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    logger.debug("JWT authentication successful for user: {} with role: {}", userEmail, role);
                    
                } else {
                    logger.warn("JWT token is invalid for user: {}", userEmail);
                }
            } else {
                logger.debug("JWT processing skipped - Email is null ({}) or authentication already exists ({})", 
                           userEmail, SecurityContextHolder.getContext().getAuthentication() != null);
            }
        } 
        catch (ExpiredJwtException e) {
            logger.warn("JWT token expired for request URI: {} - {}", request.getRequestURI(), e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, 
                              "JWT_EXPIRED", "JWT token has expired", request.getRequestURI());
            return;
        } catch (SignatureException e) {
            logger.warn("JWT signature validation failed for request URI: {} - {}", request.getRequestURI(), e.getMessage());
            handleJwtException(response, HttpServletResponse.SC_UNAUTHORIZED, 
                              "JWT_INVALID_SIGNATURE", "JWT token signature is invalid", request.getRequestURI());
            return;
        } catch (Exception e) {
            logger.error("Unexpected error in JWT processing for request URI: {} - {}", request.getRequestURI(), e.getMessage(), e);
            handleJwtException(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, 
                              "JWT_PROCESSING_ERROR", "Error processing JWT token", request.getRequestURI());
            return;
        }

        logger.debug("JWT filter processing completed for URI: {}", request.getRequestURI());
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
            
            logger.debug("JWT exception response sent - Status: {}, Error: {}, Path: {}", status, error, path);
        } catch (IOException e) {
            logger.error("Error writing JWT exception response: {}", e.getMessage(), e);
        }
    }
}
