package com.example.leave_app.controller;

import java.security.Principal;

import java.math.BigDecimal;
import java.util.Map;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.config.JwtBlacklist;
import com.example.leave_app.dao.request.ChangePasswordRequest;
import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.service.LeaveApplicationService;
import com.example.leave_app.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
@SecurityRequirement(name = "bearerAuth")
public class UserController {
    @Autowired
    private final JwtBlacklist jwtBlacklist;

    @Autowired
    private final UserService userService;

    @Autowired
    private final LeaveApplicationService leaveApplicationService;

    @PostMapping

    public ResponseEntity<?> chagePassword(@RequestBody ChangePasswordRequest password, Principal connectUser) {
        boolean passwordChanged = userService.changePassword(password, connectUser);

        if (passwordChanged) {
            return ResponseEntity.ok("Password changed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to change password");
        }
    }

    @PostMapping("/logout")

    public ResponseEntity<String> logout(HttpServletRequest reques) {
        String authHeader = reques.getHeader("Authorization");
        String jwt_Token = authHeader.substring(7);
        jwtBlacklist.blacklistToken(jwt_Token);
        return ResponseEntity.ok("Logout successfully");
    }

    @PostMapping("/createLeaveApplication")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<LeaveApplicationResponce> createLeaveApplication(
            @Valid @RequestBody LeaveApplicationRequest leaveApplicationRequest) {
        System.out.println("leaveApplicationRequest = " + leaveApplicationRequest.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveApplicationService
                .createLeaveApplication(leaveApplicationRequest));
    }

    @GetMapping("/getLeaveBalance/{userId}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<Map<String, BigDecimal>> getLeaveBalance(@PathVariable int userId) {
        Map<String, BigDecimal> leaveBalance = leaveApplicationService.getLeaveBalanceByUser(userId);
        return new ResponseEntity<>(leaveBalance, HttpStatus.OK);
    }

    @GetMapping("/leaveHistory/{userId}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<LeaveApplication>> getLeaveHistory(@PathVariable int userId) {
        List<LeaveApplication> leaveHistory = leaveApplicationService
                .getLeaveApplicationsByStatusOrderByDateAsc(userId);
        // System.out.println("leaveHistory = " + leaveHistory.toString());
        return new ResponseEntity<>(leaveHistory, HttpStatus.OK);
    }
}