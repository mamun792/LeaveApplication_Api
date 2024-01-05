package com.example.leave_app.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "leave_application")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String remark;

    private String m_remark;

    @Enumerated(EnumType.STRING)
    private LeaveStatus status;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private User user;

    private int blancLeaveCount;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "leave_type_id")
    private LeaveType leaveType;

    @Override
    public String toString() {

        return "LeaveApplication{" +
                "id=" + id +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", blancLeaveCount=" + blancLeaveCount +
                '}';
    }
}