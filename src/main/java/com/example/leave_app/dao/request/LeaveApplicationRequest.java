package com.example.leave_app.dao.request;

import java.time.LocalDate;

import com.example.leave_app.entity.LeaveStatus;
import com.example.leave_app.entity.LeaveType;
import com.example.leave_app.entity.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LeaveApplicationRequest {
    @NotNull(message = "Please provide a valid fromDate")

    private LocalDate fromDate;

    @NotNull(message = "Please provide a valid fromDate")

    private LocalDate toDate;

    @NotBlank(message = "Please provide a valid reason")
    private String remark;

    private LeaveStatus status;
    @NotNull(message = "Please provide a valid user")
    private User user;

    private Integer blancLeaveCount;

    @NotNull(message = "Please provide a valid leaveType")
    private LeaveType leaveType;
}