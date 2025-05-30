package com.project.pokesearch.controller;

import com.project.pokesearch.dto.AuthResponseDTO;
import com.project.pokesearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.pokesearch.dto.LoginRequestDTO;
import com.project.pokesearch.dto.RegisterRequestDTO;
import com.project.pokesearch.model.User;
import com.project.pokesearch.repository.UserRepository;
import com.project.pokesearch.service.JwtService;

import java.time.LocalDateTime;
import java.util.Collections;

import static java.util.Collections.singletonList;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
 
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,  JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
  
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDTO registerRequest) {
        User registeredUser = userService.registerNewUser(registerRequest);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                registeredUser.getUsername(),
                "",
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );
        String jwt = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponseDTO(jwt));
    }
    
    

    
}
