package com.example.leave_app.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
//import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;

public interface LeaveApplicationService {
    LeaveApplicationResponce createLeaveApplication(LeaveApplicationRequest leaveApplicationRequest);

    Map<String, BigDecimal> getLeaveBalanceByUser(int userId);

    public Page<LeaveApplicationResponce> getLeaveApplicationsForUser(int userId, Pageable pageable);

    public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus,
            String m_remark);

    public Page<LeaveApplicationResponce> getPendingApprovals(Pageable pageable);
}