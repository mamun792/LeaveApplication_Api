package com.example.leave_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import com.example.leave_app.entity.LeaveType;

import jakarta.persistence.Tuple;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRepository extends JpaRepository<LeaveType, Integer> {
    @Query(value = "SELECT  max_leave FROM leave_type WHERE id = :id", nativeQuery = true)

    Integer findMaxLeaveByLeaveType(@Param("id") Integer id);

    @Query(value = "SELECT lt.leave_type_name AS leaveTypeName, " +
            "CASE WHEN la.leave_type_id IS NOT NULL THEN lt.max_leave - COALESCE(SUM(la.blanc_leave_count), 0) "
            +
            "ELSE lt.max_leave END AS remainingLeaveBalance " +
            "FROM leave_type lt " +
            "LEFT JOIN leave_application la ON lt.id = la.leave_type_id AND la.user_id = :userId AND la.status = 'APPROVED' "
            +
            "WHERE lt.leave_type_name IN ('sick', 'casual') " +
            "GROUP BY lt.leave_type_name, lt.max_leave, la.user_id, la.leave_type_id", nativeQuery = true)
    List<Tuple> findTotalCBlaceByUserAndLeaveTypeGroupBy(@Param("userId") int userId);
}