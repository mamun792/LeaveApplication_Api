package com.example.leave_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;

import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;
import com.example.leave_app.entity.LeaveType;
import com.example.leave_app.entity.User;
import com.example.leave_app.exception.LeaveBalanceNotFoundException;
import com.example.leave_app.exception.LeaveExceededException;
import com.example.leave_app.exception.PendingNotFoundException;
import com.example.leave_app.exception.UserNotFoundException;
import com.example.leave_app.repository.LeaveApplicationRepository;
import com.example.leave_app.repository.LeaveTypeRepository;
import com.example.leave_app.repository.UserRepository;
import com.example.leave_app.service.LeaveApplicationService;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class LeaveApplicationServiceImpl implements LeaveApplicationService {
    @Autowired
    private final UserRepository userService;

    @Autowired
    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveApplicationResponce createLeaveApplication(LeaveApplicationRequest leaveApplicationRequest) {

        User user = userService.findById(leaveApplicationRequest.getUser().getId())
                .orElseThrow(() -> {
                    throw new UserNotFoundException("User not found");
                });

        LeaveType leaveType = leaveTypeRepository.findById(leaveApplicationRequest.getLeaveType().getId())
                .orElseThrow(() -> {
                    throw new UserNotFoundException("Leave type not found");
                });

        Integer totalleaveDays = leaveApplicationRepository.findTotalCBlaceByUserAndLeaveTypeGroupBy(
                user.getId(),
                leaveType.getId());
        Integer leaveDays = calculateLeaveDays(leaveApplicationRequest.getFromDate(),
                leaveApplicationRequest.getToDate());
        int valid = validateLeaveDays(leaveDays, leaveType);

        if (totalleaveDays + valid >= leaveType.getMaxLeave()) {
            throw new LeaveExceededException("You can not take more than " + leaveType.getMaxLeave() + " leave");
        }
        System.out.println("totalleaveDays = " + totalleaveDays);
        System.out.println("maxleave = " + leaveType.getMaxLeave());
        System.out.println("leaveDays = " + leaveDays);
        System.out.println("valid = " + valid);

        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setFromDate(leaveApplicationRequest.getFromDate());
        leaveApplication.setToDate(leaveApplicationRequest.getToDate());
        leaveApplication.setRemark(leaveApplicationRequest.getRemark());
        leaveApplication.setStatus(LeaveStatus.PENDING);
        leaveApplication.setUser(user);
        leaveApplication.setBlancLeaveCount(valid);
        leaveApplication.setLeaveType(leaveApplicationRequest.getLeaveType());
        LeaveApplication insert = leaveApplicationRepository.save(leaveApplication);
        LeaveApplicationResponce response = LeaveApplicationResponce.builder().id(insert.getId())
                .fromDate(insert.getFromDate())
                .toDate(insert.getToDate())
                .remark(insert.getRemark())
                .message("Leave application created successfully")
                .build();

        return response;

    }

    private Integer calculateLeaveDays(LocalDate fromDate, LocalDate toDate) {
        int leaveDays = 0;
        for (LocalDate date = fromDate; date.isBefore(toDate); date = date.plusDays(1)) {
            if (date.getDayOfWeek().getValue() != 6 && date.getDayOfWeek().getValue() != 7) {
                leaveDays++;
            }
        }
        return leaveDays;
    }

    private int validateLeaveDays(Integer leaveDays, LeaveType leaveType) {
        if (leaveDays > leaveType.getMaxLeave()) {
            throw new LeaveExceededException(
                    "You can not take more than " + leaveType.getMaxLeave() + " leave");
        }

        return leaveDays;
    }

    @Override
    public Map<String, BigDecimal> getLeaveBalanceByUser(int userId) {
        List<Object[]> result = leaveApplicationRepository.findTotalCBlaceByUserAndLeaveTypeGroupBy(userId)
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance not found for user"));

        Map<String, BigDecimal> leaveBalanceMap = new HashMap<>();

        for (Object[] row : result) {
            String leaveType = (String) row[0];
            BigDecimal leaveBalance = (BigDecimal) row[1];

            leaveBalanceMap.put(leaveType, leaveBalance);
        }

        return leaveBalanceMap;
    }

    @Override

    public List<LeaveApplication> getLeaveApplicationsByStatusOrderByDateAsc(int userId) {
        return leaveApplicationRepository.findByUserIdOrderByFromDateAsc(userId).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No  leave application found");
        });
    }

    @Override

    public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus) {
        // LeaveApplication leaveApplication =
        // leaveApplicationRepository.findById(leaveApplicationId)
        // .orElseThrow(() -> {
        // throw new UserNotFoundException("Leave application not found");
        // });

        // leaveApplication.setStatus(approvalStatus);

        // leaveApplicationRepository.save(leaveApplication);
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId).orElseThrow();
        if (leaveApplication != null) {
            leaveApplication.setStatus(approvalStatus);
            leaveApplicationRepository.save(leaveApplication);
            return LeaveApplicationResponce.builder().build();
        }
        return null;
        // return LeaveApplicationResponce.builder().build();
    }

    @Override

    public List<LeaveApplication> getPendingApprovals() {
        List<LeaveApplication> pendingApprovals = leaveApplicationRepository.findByStatusOrderByFromDateAsc()
                .orElseThrow(() -> new PendingNotFoundException("No pending leave application found"));

        return pendingApprovals;
    }

}
