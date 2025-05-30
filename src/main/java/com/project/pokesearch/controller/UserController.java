package com.project.pokesearch.controller;

import java.security.Principal;

import com.project.pokesearch.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.dto.PasswordChangeDTO;
import com.project.pokesearch.dto.UserDTO;
import com.project.pokesearch.model.User;
import com.project.pokesearch.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        User user = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(userMapper.toDTO(user));
    }
    
    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestBody UserDTO userDTO, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        try {
            User updatedUser = userService.updateProfile(currentUser, userDTO);
            return ResponseEntity.ok(userMapper.toDTO(updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    
    
    
    @PutMapping("/me/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDTO passwordDTO, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));        
        User updatedUser = userService.changePassword(currentUser, passwordDTO.newPassword());
        return ResponseEntity.ok("Password changed successfully");
    }
}