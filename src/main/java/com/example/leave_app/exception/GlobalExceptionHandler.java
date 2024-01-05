package com.example.leave_app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.leave_app.dao.responce.ResponseModel;

import org.springframework.validation.FieldError;
// import com.example.leave_app.model.ResponseModel;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResponseModel<Object>>

            handleMethodArgumentNotValidException(
                    MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed. Please check the request.",
                errors);
    }

    @ExceptionHandler({ UserNotFoundException.class, DuplicateEmailException.class, PendingNotFoundException.class,
            LeaveExceededException.class, LeaveBalanceNotFoundException.class, NoavabialData.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseModel<Object>>

            handleNotFoundExceptions(Exception ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseModel<Object>> handleInternalServerError(Exception ex) {
        ResponseModel<Object> errorResponse = ResponseModel
                .<Object>builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .success(false)
                .errors(ex.getMessage())
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ResponseEntity<ResponseModel<Object>>

            buildErrorResponse(HttpStatus status, String message,
                    Map<String, Object> errors) {
        ResponseModel<Object> response = ResponseModel
                .<Object>builder()
                .status(status.value())
                .message(message)
                .success(false)
                .data(null)
                .build();
        return ResponseEntity.status(status).body(response);
    }
}
