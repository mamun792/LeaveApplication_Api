package com.example.leave_app.dao.request;

import lombok.Data;
import lombok.Builder;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthencationRequest {
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email should not be null")

    private String email;

    @NotBlank(message = "Password should not be null")
    private String password;
}