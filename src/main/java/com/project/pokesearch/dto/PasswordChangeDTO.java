package com.project.pokesearch.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordChangeDTO(
        @NotBlank(message = "Obecne hasło nie może być puste")
        String currentPassword,

        @NotBlank(message = "Nowe hasło nie może być puste")
        @Size(min = 6, message = "Hasło musi mieć co najmniej 6 znaków")
        String newPassword
) {}