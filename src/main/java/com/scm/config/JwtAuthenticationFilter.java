package com.scm.config;

import com.scm.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private final JwtUtil jwtUtil;

    Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            if (authHeader.startsWith("Bearer ")) {
                // Handle JWT Token
                String jwt = authHeader.substring(7);
                if (jwtUtil.validateToken(jwt)) {
                    String username = jwtUtil.extractUsername(jwt);
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password("") // No password needed for JWT validation
                            .authorities("USER") // Assign default roles/authorities
                            .build();

                    var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } else if (authHeader.startsWith("Basic ")) {
                // Handle Basic Auth
                String base64Credentials = authHeader.substring("Basic ".length()).trim();
                String credentials = new String(Base64.getDecoder().decode(base64Credentials));
                String[] values = credentials.split(":", 2);
                
                if (values.length == 2) {
                    String username = values[0];
                    String password = values[1];
                    
                    // Here you would typically validate the credentials against your user service
                    // For now, we'll just set the authentication
                    UserDetails userDetails = User.builder()
                            .username(username)
                            .password(password)
                            .authorities("USER")
                            .build();

                    var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            userDetails, password, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
