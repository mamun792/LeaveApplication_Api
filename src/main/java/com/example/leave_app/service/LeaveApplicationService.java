package com.example.leave_app.service;

import java.math.BigDecimal;
import java.util.Map;

import java.util.List;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;

public interface LeaveApplicationService {
    LeaveApplicationResponce createLeaveApplication(LeaveApplicationRequest leaveApplicationRequest);

    Map<String, BigDecimal> getLeaveBalanceByUser(int userId);

    public List<LeaveApplication> getLeaveApplicationsByStatusOrderByDateAsc(int userId);

    public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus);

    public List<LeaveApplication> getPendingApprovals();
}