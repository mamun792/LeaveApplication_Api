package com.example.leave_app.service;

import java.security.Principal;

import com.example.leave_app.dao.request.ChangePasswordRequest;
import com.example.leave_app.entity.User;

public interface UserService {
    public boolean changePassword(ChangePasswordRequest password, Principal connectUser);

    public User getUserById(int id);
}