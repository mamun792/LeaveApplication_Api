package com.example.leave_app.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.leave_app.dao.request.AuthencationRequest;
import com.example.leave_app.dao.request.RegisterRequest;
import com.example.leave_app.dao.responce.AuthencatonResponce;
import com.example.leave_app.entity.Role;
import com.example.leave_app.entity.User;
import com.example.leave_app.exception.DuplicateEmailException;
import com.example.leave_app.repository.UserRepository;
import com.example.leave_app.service.AuthencationService;
import com.example.leave_app.service.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthencationServiceImpl implements AuthencationService {
        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        @Override
        public AuthencatonResponce register(RegisterRequest registerRequest) {
                String email = registerRequest.getEmail();
                // System.out.println("email" + email);
                if (userRepository.existsByEmail(email)) {

                        throw new DuplicateEmailException("Email is already registered. Please try with another email");
                }

                var user = User.builder()
                                .fastName(registerRequest.getFastName())
                                .lastName(registerRequest.getLastName())
                                .email(registerRequest.getEmail())
                                .password(passwordEncoder.encode(registerRequest.getPassword()))
                                .role(Role.USER).build();
                userRepository.save(user);
                var jetToken = jwtService.generateToken(user);
                return AuthencatonResponce.builder().Token(jetToken)
                                .message("User register successfully")
                                .build();

        }

        @Override
        public AuthencatonResponce authencation(AuthencationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
                var jetToken = jwtService.generateToken(user);
                return AuthencatonResponce.builder().Token(jetToken)
                                .message("User login successfully")
                                .build();
        }
}