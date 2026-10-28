package com.smartfactory.auth.service;

import com.smartfactory.auth.dto.CreateUserRequest;
import com.smartfactory.auth.dto.UserResponse;
import com.smartfactory.auth.entity.User;
import com.smartfactory.auth.exception.UserAlreadyExistsException;
import com.smartfactory.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserResponse createUser(CreateUserRequest request) {

        // 1. Check whether username already exists
        if (userRepository.existsByUsername(request.username())) {
            throw new UserAlreadyExistsException(
                    "Username already exists: " + request.username());
        }

        // 2. Hash the plain-text password
        String encodedPassword =
                passwordEncoder.encode(request.password());

        // 3. Create User entity
        User user = new User();
        user.setUsername(request.username());
        user.setPassword(encodedPassword);
        user.setRole(request.role());
        user.setEnabled(true);

        // 4. Set timestamps
        var now = java.time.LocalDateTime.now();
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        // 5. Save into smart_factory_auth.users
        User savedUser = userRepository.save(user);

        // 6. Convert entity → response DTO
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole(),
                savedUser.isEnabled(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }
}