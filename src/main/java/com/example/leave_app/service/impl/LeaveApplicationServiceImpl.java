package com.example.leave_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.leave_app.dao.request.LeaveApplicationRequest;
import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.DateRange;
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

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import com.example.leave_app.service.LeaveApplicationService;
import com.example.leave_app.service.UserService;

import jakarta.persistence.Tuple;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class LeaveApplicationServiceImpl implements LeaveApplicationService {

    @Autowired
    private final LeaveApplicationRepository leaveApplicationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private final LeaveTypeRepository leaveTypeRepository;

    @Override
    @Transactional
    public LeaveApplicationResponce createLeaveApplication(LeaveApplicationRequest leaveApplicationRequest) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int userId = ((User) userDetails).getId();
        System.out.println("userId = " + userId);

        LeaveType leaveType = leaveTypeRepository.findById(leaveApplicationRequest.getLeaveType().getId())
                .orElseThrow(() -> {
                    throw new UserNotFoundException("Leave type not found");
                });

        Integer totalleaveDays = leaveApplicationRepository.findTotalCBlaceByUserAndLeaveTypeGroupBy(
                userId,
                leaveType.getId());

        Integer leaveDays = calculateLeaveDays(leaveApplicationRequest.getFromDate(),
                leaveApplicationRequest.getToDate());
        int valid = validateLeaveDays(leaveDays, leaveType);

        if (totalleaveDays + valid > leaveType.getMaxLeave()) {
            throw new LeaveExceededException("You can not take more than " + leaveType.getMaxLeave() + " leave");
        }

        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setFromDate(leaveApplicationRequest.getFromDate());
        leaveApplication.setToDate(leaveApplicationRequest.getToDate());
        leaveApplication.setRemark(leaveApplicationRequest.getRemark());
        leaveApplication.setStatus(LeaveStatus.PENDING);
        User user = new User();
        user.setId(userId);
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
    public List<LeaveTypeResponce> getLeaveBalancesByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        int userId = ((User) userDetails).getId();

        List<Tuple> leaveBalances = leaveTypeRepository.findTotalCBlaceByUserAndLeaveTypeGroupBy(userId);

        return leaveBalances.stream().map(leaveBalance -> LeaveTypeResponce.builder()
                .leaveTypeName((String) leaveBalance.get("leaveTypeName"))
                .remainingLeaveBalance((BigDecimal) leaveBalance.get("remainingLeaveBalance"))
                .build()).collect(Collectors.toList());
    }

    @Override

    public Page<LeaveApplicationResponce> getLeaveApplicationsForUserFiltered(
            int userId, DateRange dateRange, LeaveStatus status, String leaveType, Pageable pageable) {
        Page<LeaveApplication> leaveApplications = leaveApplicationRepository.findByUserIdFiltered(
                userId,
                dateRange.getStartDate(),
                dateRange.getEndDate(),
                status,
                leaveType,
                pageable);
        // System.out.println("leaveApplications = " + leaveApplications);
        return leaveApplications.map(leaveApplication -> LeaveApplicationResponce.builder()
                .id(leaveApplication.getId())
                .fromDate(leaveApplication.getFromDate())
                .toDate(leaveApplication.getToDate())
                .remark(leaveApplication.getRemark())
                .status(leaveApplication.getStatus())
                .m_remark(leaveApplication.getM_remark())
                .blankLeaveCount(leaveApplication.getBlancLeaveCount())
                .leaveType(leaveApplication.getLeaveType().getLeaveTypeName())
                .build());

    }

    @Override
    @Transactional
    public LeaveApplicationResponce approveLeaveApplication(int leaveApplicationId, LeaveStatus approvalStatus,
            String m_remark) {
        LeaveApplication leaveApplication = leaveApplicationRepository.findById(leaveApplicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave application not found"));
        if (leaveApplication.getStatus() == LeaveStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Leave application already approved. No need to approve again");
        }
        if (leaveApplication.getStatus() == LeaveStatus.REJECTED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Leave application already rejected");
        }

        leaveApplication.setStatus(approvalStatus);
        leaveApplication.setM_remark(m_remark);
        leaveApplicationRepository.save(leaveApplication);
        return LeaveApplicationResponce.builder().message("Leave application approved successfully").build();

    }

    @Override

    public Page<LeaveApplicationResponce> getPendingApprovals(Pageable pageable) {
        Page<LeaveApplication> leaveApplications = leaveApplicationRepository.findByStatusOrderByFromDateAsc(
                pageable);

        if (leaveApplications.isEmpty()) {
            throw new PendingNotFoundException("No pending leave application found");
        }

        return leaveApplications.map(leaveApplication -> LeaveApplicationResponce.builder()
                .id(leaveApplication.getId())
                .fromDate(leaveApplication.getFromDate())
                .toDate(leaveApplication.getToDate())
                .remark(leaveApplication.getRemark())
                .status(leaveApplication.getStatus())
                .blankLeaveCount(leaveApplication.getBlancLeaveCount())
                .fastName(leaveApplication.getUser().getFastName())
                .lastName(leaveApplication.getUser().getLastName())
                .leaveType(leaveApplication.getLeaveType().getLeaveTypeName())
                .build());

    }

    public LeaveApplicationResponce createCustomLeaveApplication(LeaveApplicationRequest leaveApplicationRequest) {
        User user = userService.getUserById(leaveApplicationRequest.getUser().getId());
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        LeaveType leaveType = leaveTypeRepository.findById(leaveApplicationRequest.getLeaveType().getId())
                .orElseThrow(() -> {
                    throw new UserNotFoundException("Leave type not found");
                });
        Integer leaveDays = calculateLeaveDays(leaveApplicationRequest.getFromDate(),
                leaveApplicationRequest.getToDate());
        System.out.println("leaveDays = " + leaveDays);
        System.out.println("user = " + user);
        LeaveApplication leaveApplication = new LeaveApplication();
        leaveApplication.setFromDate(leaveApplicationRequest.getFromDate());
        leaveApplication.setToDate(leaveApplicationRequest.getToDate());
        leaveApplication.setRemark(leaveApplicationRequest.getRemark());
        leaveApplication.setStatus(LeaveStatus.APPROVED);
        leaveApplication.setUser(user);
        leaveApplication.setBlancLeaveCount(leaveDays);
        leaveApplication.setLeaveType(leaveApplicationRequest.getLeaveType());
        LeaveApplication insert = leaveApplicationRepository.save(leaveApplication);
        LeaveApplicationResponce response = LeaveApplicationResponce.builder()
                .build();

        return response;
    }
}
