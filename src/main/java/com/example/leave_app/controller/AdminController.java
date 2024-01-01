package com.example.leave_app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.leave_app.dao.request.LeaveTypeRequest;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.LeaveType;
import com.example.leave_app.service.LeaveTypeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {
    @Autowired
    private final LeaveTypeService leaveTypeService;

    @PostMapping("/leaveType")
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<LeaveType> saveLeaveType(@Valid @RequestBody LeaveTypeRequest leaveTypeRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(leaveTypeService.saveLeaveType(leaveTypeRequest));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<List<LeaveTypeResponce>> getAllLeaveTypes() {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(leaveTypeService.getAllLeaveTypes());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}