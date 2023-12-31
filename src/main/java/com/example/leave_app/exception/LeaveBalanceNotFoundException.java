package com.example.leave_app.exception;

public class LeaveBalanceNotFoundException extends RuntimeException {
    public LeaveBalanceNotFoundException(String message) {
        super(message);
    }
}