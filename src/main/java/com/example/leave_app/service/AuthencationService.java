package com.example.leave_app.service;

import com.example.leave_app.dao.request.AuthencationRequest;
import com.example.leave_app.dao.request.RegisterRequest;
import com.example.leave_app.dao.responce.AuthencatonResponce;

public interface AuthencationService {
    public AuthencatonResponce register(RegisterRequest registerRequest);

    public AuthencatonResponce authencation(AuthencationRequest authencationRequest);
}