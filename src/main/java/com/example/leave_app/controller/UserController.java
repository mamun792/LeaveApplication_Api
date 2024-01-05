package com.example.leave_app.controller;

import java.security.Principal;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.config.JwtBlacklist;
import com.example.leave_app.dao.request.ChangePasswordRequest;
import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.dao.responce.ResponseModel;
import com.example.leave_app.entity.User;
//import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.service.LeaveApplicationService;
import com.example.leave_app.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

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

    @PostMapping("/changePassword")

    public ResponseEntity<ResponseModel<?>> chagePassword(@RequestBody ChangePasswordRequest password,
            Principal connectUser) {
        boolean passwordChanged = userService.changePassword(password, connectUser);
        try {
            if (passwordChanged) {
                return ResponseEntity.ok(ResponseModel.success(HttpStatus.OK, "Password changed successfully"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResponseModel.error(HttpStatus.BAD_REQUEST, "Password not changed"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/logout")

    public ResponseEntity<ResponseModel<String>> logout(HttpServletRequest reques) {
        try {
            String authHeader = reques.getHeader("Authorization");
            String jwt_Token = authHeader.substring(7);
            jwtBlacklist.blacklistToken(jwt_Token);
            return ResponseEntity.ok(ResponseModel.success(HttpStatus.OK, "Logout successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/createLeaveApplication")
    @PreAuthorize("hasAuthority('user:create')")
    public ResponseEntity<ResponseModel<LeaveApplicationResponce>> createLeaveApplication(
            @Valid @RequestBody LeaveApplicationRequest leaveApplicationRequest) {
        try {
            LeaveApplicationResponce leaveApplication = leaveApplicationService
                    .createLeaveApplication(leaveApplicationRequest);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseModel.success(HttpStatus.CREATED, leaveApplication));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseModel.error(
                    HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @GetMapping("/getLeaveBalance")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseModel<Map<String, BigDecimal>>> getLeaveBalance() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            int userId = ((User) userDetails).getId();

            Map<String, BigDecimal> leaveBalance = leaveApplicationService.getLeaveBalanceByUser(userId);

            return ResponseEntity.ok(ResponseModel.success(HttpStatus.OK, leaveBalance));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }

    }

    @GetMapping("/leaveHistory")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ResponseModel<Page<LeaveApplicationResponce>>> getLeaveHistory(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            int userId = ((User) userDetails).getId();

            Pageable pageable = PageRequest.of(pageNo, pageSize);

            Page<LeaveApplicationResponce> leaveHistory = leaveApplicationService.getLeaveApplicationsForUser(userId,
                    pageable);

            return ResponseEntity.ok(ResponseModel.success(HttpStatus.OK, leaveHistory));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

}