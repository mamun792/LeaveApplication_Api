package com.example.leave_app.service;

import java.util.List;

import com.example.leave_app.dao.request.LeaveTypeRequest;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.LeaveType;

public interface LeaveTypeService {
    LeaveType saveLeaveType(LeaveTypeRequest leaveTypeRequest);
    // LeaveType saveLeaveType(LeaveType leaveType);

    List<LeaveTypeResponce> getAllLeaveTypes();

    LeaveTypeResponce getLeaveTypeById(int id);

}