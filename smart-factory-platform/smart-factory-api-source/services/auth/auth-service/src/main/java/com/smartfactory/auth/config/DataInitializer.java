package com.smartfactory.auth.config;

import com.smartfactory.auth.entity.User;
import com.smartfactory.auth.repository.UserRepository;
import com.smartfactory.auth.util.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    @Bean
    CommandLineRunner initializeUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByUsername("admin")) {

                LocalDateTime now = LocalDateTime.now();

                User user = new User();
                user.setUsername("admin");
                user.setPassword(passwordEncoder.encode("123456"));
                user.setRole(UserRole.ADMIN);
                user.setEnabled(true);
                user.setCreatedAt(now);
                user.setUpdatedAt(now);

                userRepository.save(user);
            }

            if (!userRepository.existsByUsername("user")) {

                User user = new User();
                user.setUsername("user");
                user.setPassword(passwordEncoder.encode("abcd"));
                user.setRole(UserRole.USER);
                user.setEnabled(true);
                user.setCreatedAt(LocalDateTime.now());
                user.setUpdatedAt(LocalDateTime.now());

                userRepository.save(user);
            }
        };
    }
}