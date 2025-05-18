package com.project.pokesearch.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthResponseDTO (
    @NotBlank(message = "Token nie moze byc pusty")
    String token
) {}
