package com.project.pokesearch.service;


import lombok.AllArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class EmailService {
    
    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;
    
    public void sendEmail(String to, String subject, String content) {
        
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(mailFrom);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);
            log.info("Email sent to {}", to);
        } catch (MailException e) {
            log.error("Niew udało się wysłać e-maila na adres: {}. Błąd: {}", to, e.getMessage());
        }
    }
    
    
}
