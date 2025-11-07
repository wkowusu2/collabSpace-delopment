package com.collabspace.collabspace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private String fileUrl;

    @Column(name = "file_size")
    private Long fileSize;

    /**
     * The id of the user who uploaded the file.
     * Using UUID to be consistent with other entities (Project.createdBy, Task.id,
     * etc.)
     */

    @Column(name = "cloudinary_public_id")
    private String cloudinaryPublicId;

    @Column(name = "uploaded_by", nullable = false)
    private UUID uploadedBy;

    /**
     * Link the attachment to a Task so task detail views can include attachments.
     * Lazy fetch to avoid loading task unless needed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @JsonIgnore
    private Task task;

    @Column(name = "uploaded_at", nullable = false, updatable = false)
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        this.uploadedAt = LocalDateTime.now();
    }

    // Convenience constructors used by service layer
    public Attachment(String fileName, String fileType, String fileUrl, UUID uploadedBy) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.uploadedBy = uploadedBy;
    }

    public Attachment(String fileName, String fileType, String fileUrl, Long fileSize, UUID uploadedBy, Task task) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileUrl = fileUrl;
        this.fileSize = fileSize;
        this.uploadedBy = uploadedBy;
        this.task = task;
    }
}
