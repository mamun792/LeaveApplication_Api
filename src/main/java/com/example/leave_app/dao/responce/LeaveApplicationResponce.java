package com.example.leave_app.dao.responce;

import java.time.LocalDate;

import com.example.leave_app.entity.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveApplicationResponce {
    private int id;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String remark;
    private String m_remark;
    private LeaveStatus status;
    private int blankLeaveCount;
    private Long userId;
    private Long leaveTypeId;
    private String leaveType;
    private String fastName;
    private String lastName;
    private String message;
}