package com.collabspace.collabspace.dto;

import lombok.Data;

@Data
public class ReportRequestDto {
    private String projectId;
    private String startDate;
    private String endDate;
    private String format; // "CSV" or "PDF"
}