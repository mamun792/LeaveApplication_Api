package com.example.leave_app.dao.request;

import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LeaveTypeRequest {
  @NotBlank(message = "Leave type name cannot be blank")
  private String leaveTypeName;

  @Positive(message = "Maximum leave should be a positive number")
  private int maxLeave;

  @NotBlank(message = "Remark cannot be blank")
  private String remark;

  @Pattern(regexp = "\\d{4}", message = "Year should be a four-digit number")
  @NotBlank(message = "Year cannot be blank")
  private String year;
}