package com.example.employee_management.dto.auth;

public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private String email;
    private String username;
    private String role;

    // Constructors
    public JwtResponse() {
    }

    public JwtResponse(String token, String email, String username, String role) {
        this.token = token;
        this.email = email;
        this.username = username;
        this.role = role;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
