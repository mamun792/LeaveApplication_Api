package com.example.leave_app.exception;

public class PendingNotFoundException extends RuntimeException {
    public PendingNotFoundException(String message) {
        super(message);
    }                               
}