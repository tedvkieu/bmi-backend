package com.example.inspection.seeder;

import com.example.inspection.entity.User;
import com.example.inspection.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    @Value("${custom.data.user.fullName}")
    private String fullName;
    @Value("${custom.data.user.username}")
    private String username;
    @Value("${custom.data.user.password}")
    private String password;
    @Value("${custom.data.user.email}")
    private String email;
    @Value("${custom.data.user.role}")
    private String role;
    @Value("${custom.data.user.phone}")
    private String phone;
    @Value("${custom.data.user.note}")
    private String note;
    @Value("${custom.data.user.dob}")
    private String dob;

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Check if users already exist
        if (userRepository.count() == 0) {
            // Create admin user (only one admin allowed)
            User adminUser = new User();
            adminUser.setFullName(fullName);
            adminUser.setUsername(username);
            adminUser.setEmail(email);
            adminUser.setPasswordHash(password);
            adminUser.setRole(User.Role.valueOf(role));
            adminUser.setPhone(phone);
            adminUser.setNote(note);
            adminUser.setDob(LocalDate.parse(dob));
            adminUser.setIsActive(true);

            userRepository.save(adminUser);
        }
    }
}
