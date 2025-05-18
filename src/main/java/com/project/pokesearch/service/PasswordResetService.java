package com.project.pokesearch.service;


import com.project.pokesearch.model.PasswordResetToken;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.PasswordResetTokenRepository;
import com.project.pokesearch.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PasswordResetService {
    private UserRepository userRepository;
    private PasswordResetTokenRepository tokenRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    public boolean requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }
        User user = userOptional.get();
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
        PasswordResetToken token = new PasswordResetToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        token.setVerificationCode(generateCode());
        token.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        tokenRepository.save(token);
        
        String subject = "Password Reset";
        String message = "Hi! \nWe've learned that you want to change your password \n\nYour password reset verification code is: " + token.getVerificationCode() + "\nYour password reset token is: " + token.getToken() +  "\n\nThis code is valid for 30 minutes. \n\nGreetings from the pokedex!";
        emailService.sendEmail(user.getEmail(), subject, message);
        return true;
    }
    
    public boolean validateVerificationCode(String token, String code) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            return false;
        }
        PasswordResetToken resetToken = tokenOptional.get();
        return !resetToken.isUsed() && resetToken.getExpiryDate().isAfter(LocalDateTime.now()) && resetToken.getVerificationCode().equals(code);
    }
    
    private String generateCode() {
        Random random  = new Random();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public boolean resetPassword(String token, String code, String newPassword) {
        if (!validateVerificationCode(token, code)) {
            return false;
        }

        PasswordResetToken resetToken = tokenRepository.findByToken(token).get();
        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordChangeDate(LocalDateTime.now());
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return true;
    }
}
