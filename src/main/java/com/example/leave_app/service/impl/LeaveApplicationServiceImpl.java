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
import com.example.leave_app.exception.NoavabialData;
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

        if (totalleaveDays + valid > leaveType.getMaxLeave()) {
            throw new LeaveExceededException("You can not take more than " + leaveType.getMaxLeave() + " leave");
        }
        // System.out.println("totalleaveDays = " + totalleaveDays);
        // System.out.println("maxleave = " + leaveType.getMaxLeave());
        // System.out.println("leaveDays = " + leaveDays);
        // System.out.println("valid = " + valid);

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
                .orElseThrow(() -> new LeaveBalanceNotFoundException("Leave balance   not found for user"));

        Map<String, BigDecimal> leaveBalanceMap = new HashMap<>();

        for (Object[] row : result) {
            String leaveType = (String) row[0];
            BigDecimal leaveBalance = (BigDecimal) row[1];

            leaveBalanceMap.put(leaveType, leaveBalance);
        }

        return leaveBalanceMap;
    }

    @Override

    public List<LeaveApplicationResponce> getLeaveApplicationsForUser(int userId) {
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findByUserId(userId);

        if (leaveApplications.isEmpty()) {
            throw new NoavabialData("No leave history found");
        }
        List<LeaveApplicationResponce> leaveApplicationResponces = leaveApplications.stream()
                .map(leaveApplication -> LeaveApplicationResponce.builder()
                        .id(leaveApplication.getId())
                        .fromDate(leaveApplication.getFromDate())
                        .toDate(leaveApplication.getToDate())
                        .remark(leaveApplication.getRemark())
                        .status(leaveApplication.getStatus())
                        .blankLeaveCount(leaveApplication.getBlancLeaveCount())
                        .leaveType(leaveApplication.getLeaveType().getLeaveTypeName())
                        // .userId(leaveApplication.getUser().getId())

                        .build())
                .toList();
        return leaveApplicationResponces;
    }

    @Override

    public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave application not found"));
        leaveApplication.setStatus(approvalStatus);

        leaveApplicationRepository.save(leaveApplication);
        return LeaveApplicationResponce.builder().message("Leave application approved successfully").build();

    }

    @Override

    public List<LeaveApplicationResponce> getPendingApprovals() {
        List<LeaveApplication> leaveApplications = leaveApplicationRepository.findByStatusOrderByFromDateAsc();

        if (leaveApplications.isEmpty()) {
            throw new PendingNotFoundException("No pending leave application found");
        }

        List<LeaveApplicationResponce> leaveApplicationResponces = leaveApplications.stream()
                .map(leaveApplication -> LeaveApplicationResponce.builder()
                        .id(leaveApplication.getId())
                        .fromDate(leaveApplication.getFromDate())
                        .toDate(leaveApplication.getToDate())
                        .remark(leaveApplication.getRemark())
                        .status(leaveApplication.getStatus())
                        .blankLeaveCount(leaveApplication.getBlancLeaveCount())
                        .fastName(leaveApplication.getUser().getFastName())
                        .lastName(leaveApplication.getUser().getLastName())
                        .leaveType(leaveApplication.getLeaveType().getLeaveTypeName())
                        .build())
                .toList();

        return leaveApplicationResponces;
    }

}
