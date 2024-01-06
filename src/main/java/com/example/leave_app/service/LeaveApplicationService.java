package com.example.leave_app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.DateRange;
//import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;

public interface LeaveApplicationService {
        LeaveApplicationResponce createLeaveApplication(LeaveApplicationRequest leaveApplicationRequest);

        // Map<String, BigDecimal> getLeaveBalanceByUser(int userId);

        // public Page<LeaveApplicationResponce> getLeaveApplicationsForUser(int userId,
        // Pageable pageable);

        public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus,
                        String m_remark);

        public Page<LeaveApplicationResponce> getPendingApprovals(Pageable pageable);

        public Page<LeaveApplicationResponce> getLeaveApplicationsForUserFiltered(
                        int userId, DateRange dateRange, LeaveStatus status, String leaveType, Pageable pageable);

        public LeaveApplicationResponce createCustomLeaveApplication(LeaveApplicationRequest leaveApplicationRequest);

        public List<LeaveTypeResponce> getLeaveBalancesByUserId();
}