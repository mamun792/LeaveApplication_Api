package com.example.leave_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.example.leave_app.entity.LeaveType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {
    @Query(value = "SELECT  max_leave FROM leave_type WHERE id = :id", nativeQuery = true)

    Integer findMaxLeaveByLeaveType(@Param("id") Integer id);
}