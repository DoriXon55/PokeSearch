package com.project.pokesearch.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Nazwa użytkownika nie może być pusta")
        @Size(min = 3, max = 20, message = "Nazwa użytkownika musi mieć od 3 do 20 znaków")
        String username,

        @NotBlank(message = "Hasło nie może być puste")
        @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
        String password,

        @NotBlank(message = "Email nie może być pusty")
        @Email(message = "Nieprawidłowy format adresu email")
        String email
) {}