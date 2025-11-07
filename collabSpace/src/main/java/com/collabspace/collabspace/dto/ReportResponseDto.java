package com.collabspace.collabspace.dto;

import lombok.Data;

@Data
public class ReportResponseDto {
    private String downloadUrl;
    private String filename;
    private String message;
    private Long generatedAt;
}