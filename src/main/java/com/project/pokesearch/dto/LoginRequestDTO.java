package com.project.pokesearch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public record LoginRequestDTO (
    @NotBlank(message = "Nazwa użytkownika nie może być pusta")
    String username,

    @NotBlank(message = "Hasło nie może być puste")
    String password
){}
