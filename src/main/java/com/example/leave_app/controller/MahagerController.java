package com.example.leave_app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;
import com.example.leave_app.service.LeaveApplicationService;

@RestController
@RequestMapping("/api/v1/manager")
public class MahagerController {
    private final LeaveApplicationService leaveApplicationService;

    public MahagerController(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/pending-approvals")
    public ResponseEntity<List<LeaveApplication>> getPendingApprovals() {
        return ResponseEntity.status(HttpStatus.OK).body(leaveApplicationService.getPendingApprovals());
    }

    @PostMapping("/approve")
    public ResponseEntity<LeaveApplicationResponce> approveLeaveApplication(
            @RequestBody Map<String, Object> requestBody) {
        int leaveApplicationId = (int) requestBody.get("leaveApplicationId");
        LeaveStatus approvalStatus = LeaveStatus.valueOf((String) requestBody.get("approvalStatus"));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(leaveApplicationService.approveLeaveApplication(leaveApplicationId, approvalStatus));
    }
}