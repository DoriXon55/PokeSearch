package com.project.pokesearch.service;


import com.project.pokesearch.model.PasswordResetToken;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.PasswordResetTokenRepository;
import com.project.pokesearch.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.index.qual.LengthOf;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PasswordResetService {
    private UserRepository userRepository;
    private PasswordResetTokenRepository tokenRepository;
    private EmailService emailService;
    private PasswordEncoder passwordEncoder;
    public boolean requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            log.warn("User not found");
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

        String subject = "Reset Hasła dla PokeSearch";
        String message = "Cześć " + user.getUsername() + ",\n\nOtrzymaliśmy prośbę o zresetowanie hasła dla Twojego konta.\n\n" +
                "Twój kod weryfikacyjny to: " + token.getVerificationCode() + "\n" +
                "Twój token resetu hasła to: " + token.getToken() + "\n\n" +
                "Kod i token są ważne przez 30 minut.\n\n" +
                "Jeśli to nie Ty prosiłeś o zmianę hasła, zignoruj tę wiadomość.\n\n" +
                "Pozdrawiamy,\nZespół PokeSearch";
        emailService.sendEmail(user.getEmail(), subject, message);
        log.info("Wysłano żądanie resetu hasła dla użytkownika: {}", email);
        return true;
    }
    
    public boolean validateVerificationCode(String token, String code) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            log.warn("Próba walidacji nieistniejącego tokenu: {}", token);
            return false;
        }
        PasswordResetToken resetToken = tokenOptional.get();
        boolean isValid = !resetToken.isUsed() && 
                resetToken.getExpiryDate().isAfter(LocalDateTime.now()) &&
                resetToken.getVerificationCode().equals(code);
        
        if (!isValid) {
            log.warn("Nieudana walidacja kodu dla tokenu: {}", token);
        }
        return isValid;
    }
    
    private String generateCode() {
        SecureRandom random  = new SecureRandom();
        int code = 100000 + random.nextInt(900000);
        return String.valueOf(code);
    }

    public boolean resetPassword(String token, String code, String newPassword) {
        if (!validateVerificationCode(token, code)) {
            return false;
        }
        
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        if (tokenOptional.isEmpty()) {
            log.error("Krytyczny błąd: Token {} przeszedł walidację, ale nie został znaleziony podczas resetowania hasła.", token);
            return false;
        }
        PasswordResetToken resetToken = tokenOptional.get();
        User user = resetToken.getUser();
        
        if (user == null){
            log.error("Krytyczny błąd: Token {} nie ma przypisanego użytkownika.", token);
            return false;
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setLastPasswordChangeDate(LocalDateTime.now());
        userRepository.save(user);
        resetToken.setUsed(true);
        tokenRepository.save(resetToken);
        log.info("Hasło dla użytkownika {} zostało zresetowane pomyślnie", user.getEmail());

        return true;
    }
}
