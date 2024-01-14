package com.example.leave_app.controller;

//import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.dao.responce.ResponseModel;
import com.example.leave_app.entity.LeaveStatus;
import com.example.leave_app.exception.PendingNotFoundException;
import com.example.leave_app.service.LeaveApplicationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/v1/manager")
@PreAuthorize("hasAnyRole('MANAGER')")
@SecurityRequirement(name = "bearerAuth")
public class ManagerController {

    private final LeaveApplicationService leaveApplicationService;

    public ManagerController(LeaveApplicationService leaveApplicationService) {
        this.leaveApplicationService = leaveApplicationService;
    }

    @GetMapping("/pending-approvals")
    @PreAuthorize("hasAnyAuthority('manager:read', 'admin:read')")
    public ResponseEntity<ResponseModel<Page<LeaveApplicationResponce>>> getPendingApprovals(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize) {
        try {

            Pageable pageable = PageRequest.of(pageNo, pageSize);
            Page<LeaveApplicationResponce> pendingApprovals = leaveApplicationService.getPendingApprovals(pageable);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseModel.success(HttpStatus.OK, pendingApprovals));

        } catch (PendingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ResponseModel.error(HttpStatus.NOT_FOUND, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/approve")
    @PreAuthorize("hasAnyAuthority('manager:update','admin:update')")
    public ResponseEntity<ResponseModel<LeaveApplicationResponce>> approveLeaveApplication(
            @RequestParam int leaveApplicationId,
            @RequestParam LeaveStatus approvalStatus,
            @RequestParam String m_remark) {
        try {
            LeaveApplicationResponce approvedApplication = leaveApplicationService
                    .approveLeaveApplication(leaveApplicationId, approvalStatus, m_remark);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseModel.success(HttpStatus.OK, approvedApplication));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }

    @PostMapping("/custom")
    @PreAuthorize("hasAnyAuthority('manager:create', 'admin:create')")
    public ResponseEntity<ResponseModel<LeaveApplicationResponce>> customLeaveCreate(
            @Valid @RequestBody LeaveApplicationRequest leaveApplicationRequest) {
        try {
            LeaveApplicationResponce leaveApplicationResponce = leaveApplicationService
                    .createCustomLeaveApplication(leaveApplicationRequest);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseModel.success(HttpStatus.OK, leaveApplicationResponce));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseModel.error(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
        }
    }
}
