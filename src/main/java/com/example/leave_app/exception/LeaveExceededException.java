package com.example.leave_app.exception;

public class LeaveExceededException extends RuntimeException {
    public LeaveExceededException(String message) {
        super(message);
    }
}