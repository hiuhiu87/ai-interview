package com.aiinterview.cms.dto.admin;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserRequest(
        @NotBlank String fullName,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "ADMIN|INTERVIEWER") String role
) {
}
