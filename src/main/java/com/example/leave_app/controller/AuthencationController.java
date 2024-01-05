package com.example.leave_app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.request.AuthencationRequest;

import com.example.leave_app.dao.request.RegisterRequest;
import com.example.leave_app.dao.responce.AuthencatonResponce;
import com.example.leave_app.dao.responce.ResponseModel;
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

    public ResponseEntity<ResponseModel<AuthencatonResponce>> register(
            @Valid @RequestBody RegisterRequest registerRequest) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.success(HttpStatus.OK,
                    authencationService.register(registerRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @PostMapping("/authentication")

    public ResponseEntity<ResponseModel<AuthencatonResponce>> authentication(
            @Valid @RequestBody AuthencationRequest authencationRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseModel.success(HttpStatus.OK,
                    authencationService.authencation(authencationRequest)));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResponseModel.error(HttpStatus.BAD_REQUEST, e.getMessage()));
        }

    }
}