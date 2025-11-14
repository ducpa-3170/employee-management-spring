package com.example.employee_management.seeder;

import com.example.employee_management.config.AdminSeederConfig;
import com.example.employee_management.models.Role;
import com.example.employee_management.models.User;
import com.example.employee_management.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private final UserRepository userRepository;
    private final AdminSeederConfig adminConfig;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository,
            AdminSeederConfig adminConfig,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.adminConfig = adminConfig;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedAdminUser();
    }

    private void seedAdminUser() {
        String adminEmail = adminConfig.getEmail();
        logger.info("Checking for existing admin user with email: {}", adminEmail);
        if (!userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setUsername(adminConfig.getUsername());
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode(adminConfig.getPassword()));
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);

            userRepository.save(admin);
            logger.info("Admin user created successfully!");
            logger.info("   Username: {}", adminConfig.getUsername());
            logger.info("   Email: {}", adminEmail);
        } else {
            logger.info("Admin user already exists with email: {}", adminEmail);
        }
    }
}
