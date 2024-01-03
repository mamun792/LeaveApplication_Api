package com.example.leave_app.mapper;

import org.springframework.stereotype.Component;

import com.example.leave_app.dao.request.LeaveTypeRequest;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.LeaveType;

@Component
public class LeaveTypeMapper {
    public static LeaveType mapLeaveTypeRequestToLeaveType(LeaveTypeRequest leaveTypeRequest) {
        return LeaveType.builder()
                .leaveTypeName(leaveTypeRequest.getLeaveTypeName())
                .maxLeave(leaveTypeRequest.getMaxLeave())
                .remark(leaveTypeRequest.getRemark())
                .year(leaveTypeRequest.getYear())
                .build();
    }

    public static LeaveTypeResponce mapLeaveTypeToLeaveTypeResponce(LeaveType leaveType) {
        return LeaveTypeResponce.builder()
                .id(leaveType.getId())
                .leaveTypeName(leaveType.getLeaveTypeName())
                .remark(leaveType.getRemark())
                .maxLeave(leaveType.getMaxLeave())
                .year(leaveType.getYear())
                .build();
    }
}