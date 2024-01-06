package com.example.leave_app.repository;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import com.example.leave_app.entity.LeaveApplication;
import com.example.leave_app.entity.LeaveStatus;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

        @Query(value = "SELECT  COALESCE(SUM(blanc_leave_count),0) FROM leave_application WHERE user_id = :user_id AND leave_type_id = :leave_type_id AND status IN ('APPROVED', 'PENDING')", nativeQuery = true)

        Integer findTotalCBlaceByUserAndLeaveTypeGroupBy(@Param("user_id") Integer userId,
                        @Param("leave_type_id") Integer leaveTypeId);

        // @Query(value = "SELECT lt.leave_type_name AS leave_type,
        // SUM(la.blanc_leave_count) AS leaveBalance " +
        // "FROM leave_application la " +
        // "JOIN leave_type lt ON la.leave_type_id = lt.id " +
        // "WHERE la.user_id = :userId AND la.status = 'APPROVED' " +
        // "GROUP BY lt.leave_type_name", nativeQuery = true)
        // Optional<List<Object[]>>

        // findTotalCBlaceByUserAndLeaveTypeGroupBy(@Param("userId") int userId);

        @Query("SELECT la FROM LeaveApplication la JOIN FETCH la.leaveType lt " +
                        "WHERE la.user.id = :userId " +
                        "AND (:startDate IS NULL OR la.fromDate >= :startDate) " +
                        "AND (:endDate IS NULL OR la.toDate <= :endDate) " +
                        "AND (:leaveType IS NULL OR lt.leaveTypeName = :leaveType) " +
                        "AND (:status IS NULL OR la.status = :status)")
        Page<LeaveApplication> findByUserIdFiltered(
                        @Param("userId") int userId,
                        @Param("startDate") LocalDate startDate,
                        @Param("endDate") LocalDate endDate,
                        @Param("status") LeaveStatus status,
                        @Param("leaveType") String leaveType,
                        Pageable pageable);

        @Query("SELECT la FROM LeaveApplication la JOIN FETCH la.leaveType lt JOIN FETCH la.user u WHERE la.status = 'PENDING'")

        Page<LeaveApplication> findByStatusOrderByFromDateAsc(Pageable pageable);
}