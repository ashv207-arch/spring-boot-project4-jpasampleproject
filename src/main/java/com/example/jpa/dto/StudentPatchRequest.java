package com.example.jpa.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.Set;

public record StudentPatchRequest(
        @Size(max = 120) String name,
        @Email String email,
        @PositiveOrZero Long fees,
        Long departmentId,
        Set<Long> courseIds
) {
}
