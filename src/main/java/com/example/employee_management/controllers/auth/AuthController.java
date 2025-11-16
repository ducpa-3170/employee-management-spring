package com.example.employee_management.controllers.auth;

import com.example.employee_management.dto.auth.JwtRequest;
import com.example.employee_management.dto.auth.JwtResponse;
import com.example.employee_management.models.User;
import com.example.employee_management.security.JwtTokenUtil;
import com.example.employee_management.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("error", "Invalid username or password!");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "pages/auth/login";
    }

    /**
     * REST API endpoint để login và nhận JWT token
     * POST /auth/api/login
     * Body: { "email": "user@example.com", "password": "password" }
     * Response: { "token": "jwt_token", "email": "user@example.com", "username": "username", "role": "ADMIN" }
     */
    @PostMapping("/api/login")
    @ResponseBody
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody JwtRequest authenticationRequest) {
        try {
            // Authenticate user
            authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());

            // Load user details
            final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getEmail());

            // Get user info
            final User user = userService.findByEmail(authenticationRequest.getEmail());

            // Generate token
            final String token = jwtTokenUtil.generateToken(userDetails);

            // Return response với token và user info
            JwtResponse response = new JwtResponse(
                token,
                user.getEmail(),
                user.getUsername(),
                user.getRole().name()
            );

            return ResponseEntity.ok(response);

        } catch (DisabledException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "User is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Invalid credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentication failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * REST API endpoint để refresh JWT token
     * POST /auth/api/refresh
     * Header: Authorization: Bearer <old_token>
     * Response: { "token": "new_jwt_token" }
     */
    @PostMapping("/api/refresh")
    @ResponseBody
    public ResponseEntity<?> refreshAuthenticationToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);

                // Refresh token
                String refreshedToken = jwtTokenUtil.refreshToken(token);

                Map<String, String> response = new HashMap<>();
                response.put("token", refreshedToken);

                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Invalid authorization header");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token refresh failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

    /**
     * REST API endpoint để kiểm tra token có hợp lệ không
     * GET /auth/api/validate
     * Header: Authorization: Bearer <token>
     * Response: { "valid": true/false }
     */
    @GetMapping("/api/validate")
    @ResponseBody
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                String username = jwtTokenUtil.getUsernameFromToken(token);
                UserDetails userDetails = userService.loadUserByUsername(username);

                boolean isValid = jwtTokenUtil.validateToken(token, userDetails);

                Map<String, Object> response = new HashMap<>();
                response.put("valid", isValid);
                response.put("username", username);

                return ResponseEntity.ok(response);
            } else {
                Map<String, Boolean> response = new HashMap<>();
                response.put("valid", false);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Map<String, Boolean> response = new HashMap<>();
            response.put("valid", false);
            return ResponseEntity.ok(response);
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
