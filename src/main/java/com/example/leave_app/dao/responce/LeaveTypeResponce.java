package com.example.leave_app.dao.responce;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveTypeResponce {
    private int id;
    private String leaveTypeName;
    private int maxLeave;
    private String remark;
    private String year;
    private BigDecimal remainingLeaveBalance;
    private List<LeaveApplicationResponce> leaveApplications;
}