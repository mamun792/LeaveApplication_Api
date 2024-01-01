package com.example.leave_app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.request.AuthencationRequest;
import com.example.leave_app.dao.request.RegisterRequest;
import com.example.leave_app.dao.responce.AuthencatonResponce;

import com.example.leave_app.service.AuthencationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthencationController {
    @Autowired
    private AuthencationService authencationService;

    @PostMapping("/register")

    public ResponseEntity<AuthencatonResponce> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authencationService.register(registerRequest));
    }

    @PostMapping("/authentication")

    public ResponseEntity<AuthencatonResponce> register(@Valid @RequestBody AuthencationRequest authencationRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(authencationService.authencation(authencationRequest));
    }
}