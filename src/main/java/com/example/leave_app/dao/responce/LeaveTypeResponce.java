package com.example.leave_app.dao.responce;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveTypeResponce {
    private int id;
    private String leaveTypeName;
    private int maxLeave;

    private String year;
    private List<LeaveApplicationResponce> leaveApplications;
}