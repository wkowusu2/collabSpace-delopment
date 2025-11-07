package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class AttachmentDto {
    private Long id;
    private String fileName;
    private String fileUrl;
    private String viewUrl; // URL to view the file inline (for PDFs)
    private String fileType;
    private Long fileSize;
    private String cloudinaryPublicId;
    private UUID uploadedBy;
    private LocalDateTime uploadedAt;
}