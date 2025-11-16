package com.example.employee_management.dto.auth;

import com.example.employee_management.models.Role;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private boolean enabled;

    // Constructors
    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, Role role, boolean enabled) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.enabled = enabled;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRoleName() {
        return role != null ? role.name() : "";
    }
}
