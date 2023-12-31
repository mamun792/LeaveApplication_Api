package com.example.leave_app.service.impl;

import lombok.RequiredArgsConstructor;

import java.security.Principal;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.leave_app.exception.UserNotFoundException;
import com.example.leave_app.dao.request.ChangePasswordRequest;
import com.example.leave_app.entity.User;

import com.example.leave_app.repository.UserRepository;
import com.example.leave_app.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServicImpl implements UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    @Override
    public boolean changePassword(ChangePasswordRequest password, Principal connectUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectUser).getPrincipal();
        // check if the old password is correct
        if (!passwordEncoder.matches(password.getOldPassword(), user.getPassword())) {
            return false;
        }
        // check if the new password and confirm password are the same
        if (!password.getNewPassword().equals(password.getConfirmPassword())) {
            return false;
        }
        // all condition match, change the password
        user.setPassword(passwordEncoder.encode(password.getNewPassword()));
        // save the new password
        userRepository.save(user);
        return true;
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElseThrow(() -> {
            throw new UserNotFoundException("User not found");
        });
    }
}
