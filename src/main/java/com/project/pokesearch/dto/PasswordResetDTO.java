package com.project.pokesearch.dto;

public record PasswordResetDTO(
        String token,
        String code,
        String newPassword
) {}