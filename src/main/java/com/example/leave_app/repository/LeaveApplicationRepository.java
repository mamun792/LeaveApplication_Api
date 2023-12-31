package com.example.leave_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

//import com.example.leave_app.dao.responce.LeaveApplicationResponce;
import com.example.leave_app.entity.LeaveApplication;

@Repository
public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {

        @Query(value = "SELECT  COALESCE(SUM(blanc_leave_count),0) FROM leave_application WHERE user_id = :user_id AND leave_type_id = :leave_type_id AND status IN ('APPROVED', 'PENDING')", nativeQuery = true)

        Integer findTotalCBlaceByUserAndLeaveTypeGroupBy(@Param("user_id") Integer userId,
                        @Param("leave_type_id") Integer leaveTypeId);

        @Query(value = "SELECT lt.leave_type_name AS leave_type, SUM(la.blanc_leave_count) AS leaveBalance " +
                        "FROM leave_application la " +
                        "JOIN leave_type lt ON la.leave_type_id = lt.id " +
                        "WHERE la.user_id = :userId " +
                        "GROUP BY lt.leave_type_name", nativeQuery = true)
        Optional<List<Object[]>>

                        findTotalCBlaceByUserAndLeaveTypeGroupBy(@Param("userId") int userId);

        @Query(value = "SELECT * FROM leave_application WHERE user_id = :userId ORDER BY from_date ASC", nativeQuery = true)
        Optional<List<LeaveApplication>> findByUserIdOrderByFromDateAsc(@Param("userId") int userId);

        // @Query(value = "SELECT id, from_date, to_date, status,remark, user_id,
        // blanc_leave_count, leave_type_id FROM leave_application WHERE status =
        // 'PENDING'", nativeQuery = true)
        @Query(value = "SELECT * FROM leave_application WHERE status = 'PENDING'", nativeQuery = true)
        Optional<List<LeaveApplication>> findByStatusOrderByFromDateAsc();
}