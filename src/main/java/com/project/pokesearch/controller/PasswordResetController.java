package com.project.pokesearch.controller;


import com.project.pokesearch.dto.PasswordResetDTO;
import com.project.pokesearch.dto.PasswordResetRequestDTO;
import com.project.pokesearch.dto.VerifyCodeDTO;
import com.project.pokesearch.service.PasswordResetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/password")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;
    
    public PasswordResetController(PasswordResetService passwordResetService) { this.passwordResetService = passwordResetService; }
    
    @PostMapping("/reset-request")
    public ResponseEntity<?> requestReset(@RequestBody PasswordResetRequestDTO requestDTO)
    {
        boolean result = passwordResetService.requestPasswordReset(requestDTO.email());
        if (result) {
            return ResponseEntity.ok(Map.of("message", "Password reset email sent"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not found"));
        }
    }
    
    
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody VerifyCodeDTO verifyDTO)
    {
        boolean isValid = passwordResetService.validateVerificationCode(verifyDTO.token(), verifyDTO.code());
        if (isValid) {
            return ResponseEntity.ok(Map.of("valid", true));
        } else {
            return ResponseEntity.badRequest().body(Map.of("valid", false, "error", "Invalid or expired verification code"));
        }
    }
    
    
    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody PasswordResetDTO resetDTO)
    {
        boolean result = passwordResetService.resetPassword(resetDTO.token(), resetDTO.code(), resetDTO.newPassword());
        if (result) {
            return ResponseEntity.ok(Map.of("message", "Password reset successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to reset password. Invalid or expired code."));
        }
    }
}
