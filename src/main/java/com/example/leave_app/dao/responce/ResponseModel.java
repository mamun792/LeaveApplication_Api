package com.example.leave_app.dao.responce;

import org.springframework.http.HttpStatus;

//import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseModel<T> {
    private int status;
    private boolean success;
    private Object data;
    private String message;
    private String errors;

    public static <T> ResponseModel<T> error(HttpStatus status, String message) {
        return ResponseModel.<T>builder()
                .status(status.value())
                .success(false)
                .errors(message)
                .build();
    }

    public static <T> ResponseModel<T> success(HttpStatus status, T data) {
        return ResponseModel.<T>builder()
                .status(status.value())
                .success(true)
                .data(data)
                .build();
    }
}