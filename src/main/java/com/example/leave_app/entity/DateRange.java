package com.example.leave_app.entity;

import java.time.LocalDate;

import lombok.Data;

import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor

public class DateRange {
    private LocalDate startDate;
    private LocalDate endDate;

}