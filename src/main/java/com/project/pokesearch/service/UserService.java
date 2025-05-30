package com.project.pokesearch.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import com.project.pokesearch.dto.RegisterRequestDTO;
import com.project.pokesearch.dto.UserDTO;
import com.project.pokesearch.exception.EmailAlreadyInUseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public User updateProfile(User user, UserDTO userDTO) {
        if (!user.getEmail().equals(userDTO.email()) && existsByEmail(userDTO.email())) {
            
            throw new EmailAlreadyInUseException("Email already exists");
        }
        user.setEmail(userDTO.email());
        return userRepository.save(user);
    }
    
    public User changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordChangeDate(LocalDateTime.now());
        return userRepository.save(user);
    }

    public User registerNewUser(RegisterRequestDTO registerRequest) {
        if (userRepository.existsByUsername(registerRequest.username())) {
            throw new RuntimeException("Username is already in use");
        }
        if (userRepository.existsByEmail(registerRequest.email())) {
            throw new EmailAlreadyInUseException("Email already exists");
        }

        User user = new User();
        user.setUsername(registerRequest.username());
        user.setEmail(registerRequest.email());
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }
}