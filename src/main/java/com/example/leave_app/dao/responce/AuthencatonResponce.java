package com.example.leave_app.dao.responce;

import lombok.Data;
import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthencatonResponce {
    private String Token;
    private String message;
}