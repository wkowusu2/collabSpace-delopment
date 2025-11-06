package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.AttachmentDto;
import com.collabspace.collabspace.entity.Attachment;
import com.collabspace.collabspace.services.AttachmentService;
import com.collabspace.collabspace.services.AuthorizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attachments")
@AllArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    private final AuthorizationService authorizationService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "taskId", required = false) UUID taskId,
            @RequestHeader("X-User-Id") UUID userId) {
        try {
            Attachment saved = attachmentService.uploadFile(file, userId, taskId);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", ex.getMessage()));
        } catch (IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    Map.of("error", "Failed to upload file", "details", ex.getMessage()));
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttachment(@PathVariable Long id) {
        try {
            Attachment attachment = attachmentService.getAttachmentById(id);
            return ResponseEntity.ok(attachment);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<?> getAttachmentsByTask(@PathVariable UUID taskId) {
        try {
            List<Attachment> attachments = attachmentService.getAttachmentsByTask(taskId);

            List<AttachmentDto> attachmentDtos = attachments.stream()
                    .map(this::convertToAttachmentDto)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(attachmentDtos);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAttachment(
            @PathVariable Long id,
            @RequestHeader("X-User-Id") UUID userId) {
        try {
            if (!authorizationService.canDeleteAttachment(userId, id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        Map.of(
                                "error",
                                "Forbidden: You do not have permission to delete this attachment. " +
                                        "Only the uploader or users with PROJECT_ADMIN / PROJECT_MANAGER role can delete this attachment."));
            }

            attachmentService.deleteAttachment(id);
            return ResponseEntity.ok(Map.of("status", "success", "message", "Attachment deleted successfully"));

        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", ex.getMessage()));
        }
    }

    private AttachmentDto convertToAttachmentDto(Attachment attachment) {
        AttachmentDto dto = new AttachmentDto();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileUrl(attachment.getFileUrl());
        dto.setFileType(attachment.getFileType());
        dto.setFileSize(attachment.getFileSize());
        dto.setUploadedBy(attachment.getUploadedBy());
        dto.setUploadedAt(attachment.getUploadedAt());
        return dto;
    }
}