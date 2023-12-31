package com.example.leave_app.service.impl;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.leave_app.dao.request.LeaveTypeRequest;
import com.example.leave_app.dao.responce.LeaveTypeResponce;
import com.example.leave_app.entity.LeaveType;
import com.example.leave_app.exception.UserNotFoundException;
import com.example.leave_app.mapper.LeaveTypeMapper;
import com.example.leave_app.repository.LeaveTypeRepository;
import com.example.leave_app.service.LeaveTypeService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class LeaveTypeServiceImpl implements LeaveTypeService {
    private final LeaveTypeRepository leaveTypeRepository;

    @Transactional
    public LeaveType saveLeaveType(LeaveTypeRequest leaveTypeRequest) {
        try {

            LeaveType leaveType = LeaveTypeMapper.mapLeaveTypeRequestToLeaveType(leaveTypeRequest);

            return leaveTypeRepository.save(leaveType);

        } catch (Exception e) {
            throw new UserNotFoundException("Failed to save leave type. Please try again later." + e.getMessage());
        }
    }

    public List<LeaveTypeResponce> getAllLeaveTypes() {
        try {
            List<LeaveType> leaveTypes = leaveTypeRepository.findAll();

            if (leaveTypes.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Leave types not found");
            }

            return leaveTypes.stream().map(LeaveTypeMapper::mapLeaveTypeToLeaveTypeResponce)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new UserNotFoundException("Failed to get leave types. Please try again later." + e.getMessage());
        }
    }

    public LeaveTypeResponce getLeaveTypeById(int id) {
        try {
            LeaveType leaveType = leaveTypeRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("Leave type not found"));

            return LeaveTypeMapper.mapLeaveTypeToLeaveTypeResponce(leaveType);
        } catch (Exception e) {
            throw new UserNotFoundException("Failed to get leave type. Please try again later." + e.getMessage());
        }
    }

}
