package com.example.jpa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record StudentRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Email String email,
        @NotNull @PositiveOrZero Long fees,
        Long departmentId,
        Set<Long> courseIds
) {
}
