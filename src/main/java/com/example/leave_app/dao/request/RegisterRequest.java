package com.example.leave_app.dao.request;

import lombok.Data;
import lombok.Builder;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "fastName is mandatory")
    private String fastName;
    @NotBlank(message = "lastName is mandatory")
    private String lastName;
    @NotBlank(message = "email is mandatory")
    @Email(message = "email is not valid")
    private String email;
    @NotBlank(message = "password is mandatory")
    @Column(unique = true)
    private String password;

}