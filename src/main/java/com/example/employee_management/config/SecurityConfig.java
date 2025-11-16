package com.example.employee_management.config;

import com.example.employee_management.security.JwtAuthenticationFilter;
import com.example.employee_management.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        // Disable CSRF cho API endpoints sử dụng JWT
                        .ignoringRequestMatchers("/auth/api/**", "/api/**"))
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authz -> authz
                        // Public endpoints
                        .requestMatchers("/auth/login", "/auth/register", "/auth/api/**", 
                                "/css/**", "/js/**", "/img/**", "/vendor/**",
                                "/scss/**", "/favicon.ico", "/favicon.svg", "/error", "/access-denied")
                        .permitAll()
                        // API endpoints - cần JWT authentication
                        .requestMatchers("/api/**").authenticated()
                        // Employee list - accessible by all authenticated users (including USER role)
                        .requestMatchers("/admin/employees").hasAnyRole("ADMIN", "USER")
                        // Admin endpoints - full CRUD
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // All other requests require authentication
                        .anyRequest().authenticated())
                // Form login cho web UI
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .usernameParameter("email")
                        .loginProcessingUrl("/auth/login")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/auth/login?error=true")
                        .permitAll())
                // Logout
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .logoutSuccessUrl("/auth/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                // Remember me
                .rememberMe(remember -> remember
                        .key("uniqueAndSecret")
                        .tokenValiditySeconds(86400) // 1 day
                )
                // Exception handling
                .exceptionHandling(ex -> ex
                        .accessDeniedPage("/access-denied"))
                // Session management - STATELESS cho JWT
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Thêm JWT filter trước UsernamePasswordAuthenticationFilter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
