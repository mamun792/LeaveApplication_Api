package com.example.leave_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.responce.LeaveApplicationResponce;

import com.example.leave_app.entity.LeaveStatus;
import com.example.leave_app.service.LeaveApplicationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/manager")
@PreAuthorize("hasAnyRole('MANAGER')")
@SecurityRequirement(name = "bearerAuth")
public class MahagerController {
    @Autowired
    private final LeaveApplicationService leaveApplicationService;

    public MahagerController(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/pending-approvals")
    @PreAuthorize("hasAuthority('manager:read')")
    public ResponseEntity<List<LeaveApplicationResponce>> getPendingApprovals() {
        return ResponseEntity.status(HttpStatus.OK).body(leaveApplicationService.getPendingApprovals());
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAuthority('manager:update')")
    public ResponseEntity<LeaveApplicationResponce> approveLeaveApplication(
            @RequestParam int leaveApplicationId,
            @RequestParam LeaveStatus approvalStatus) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(leaveApplicationService.approveLeaveApplication(leaveApplicationId, approvalStatus));
    }
}
