package com.example.leave_app.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leave_type")

public class LeaveType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String leaveTypeName;

  private int maxLeave;
  private String remark;
  private String year;

  @OneToMany(mappedBy = "leaveType", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JsonIgnore
  @Builder.Default
  private List<LeaveApplication> leaveApplications = new ArrayList<LeaveApplication>();
}