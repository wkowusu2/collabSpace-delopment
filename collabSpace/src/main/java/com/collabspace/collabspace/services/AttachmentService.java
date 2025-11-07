package com.collabspace.collabspace.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.collabspace.collabspace.entity.Attachment;
import com.collabspace.collabspace.entity.Task;
import com.collabspace.collabspace.repository.AttachmentRepository;
import com.collabspace.collabspace.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final Cloudinary cloudinary;

    private static final Set<String> ALLOWED_MIME_TYPES = Set.of(
            "application/pdf",
            "image/png",
            "image/jpeg",
            "image/jpg");

    private final long maxFileSizeBytes;

    public AttachmentService(AttachmentRepository attachmentRepository,
            TaskRepository taskRepository,
            Cloudinary cloudinary,
            @Value("${file.max-size-bytes:10485760}") long maxFileSizeBytes) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.cloudinary = cloudinary;
        this.maxFileSizeBytes = maxFileSizeBytes;
    }

//    public Attachment uploadFile(MultipartFile file, UUID uploadedBy, UUID taskId) throws IOException {
//        validateFile(file);
//
//        Task task = null;
//        if (taskId != null) {
//            task = taskRepository.findById(taskId)
//                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
//        }
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> uploadResult = cloudinary.uploader()
//                .upload(file.getBytes(), ObjectUtils.asMap(
//                        "resource_type", "auto",
//                        "folder", "collabspace/attachments"
//                        ));
//
//        String fileUrl = Objects.toString(uploadResult.get("secure_url"), null);
//        String publicId = Objects.toString(uploadResult.get("public_id"), null);
//
//        if (fileUrl == null) {
//            throw new RuntimeException("Failed to obtain uploaded file URL from Cloudinary");
//        }
//
//        Attachment attachment = new Attachment();
//        attachment.setFileName(file.getOriginalFilename());
//        attachment.setFileType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
//        attachment.setFileUrl(fileUrl);
//        attachment.setCloudinaryPublicId(publicId);
//        attachment.setFileSize(file.getSize());
//        attachment.setUploadedBy(uploadedBy);
//        attachment.setTask(task);
//
//        return attachmentRepository.save(attachment);
//    }

    public Attachment uploadFile(MultipartFile file, UUID uploadedBy, UUID taskId) throws IOException {
        validateFile(file);

        Task task = null;
        if (taskId != null) {
            task = taskRepository.findById(taskId)
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        }

        // --- determine Cloudinary resource type dynamically ---
        String contentType = file.getContentType();
        String resourceType = "auto"; // default fallback

//        if (contentType != null) {
//            if (contentType.startsWith("image/")) {
//                resourceType = "image";
//            } else if (contentType.startsWith("video/")) {
//                resourceType = "video";
//            } else {
//                resourceType = "raw"; // for PDFs, docs, etc.
//            }
//        }

        if (contentType != null) {
            if (contentType.startsWith("image/")) {
                resourceType = "image";
            } else if (contentType.startsWith("video/")) {
                resourceType = "video";
            } else {
                resourceType = "raw"; // for PDFs and other files
            }
        }


        // --- perform upload ---
        @SuppressWarnings("unchecked")
        Map<String, Object> uploadResult = cloudinary.uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "resource_type", resourceType,
                        "folder", "collabspace/attachments"
                ));

        String publicId = Objects.toString(uploadResult.get("public_id"), null);

        if (publicId == null) {
            throw new RuntimeException("Failed to obtain public ID from Cloudinary");
        }

        String fileUrl = Objects.toString(uploadResult.get("secure_url"), null);

        if (fileUrl == null) {
            throw new RuntimeException("Failed to obtain uploaded file URL from Cloudinary");
        }

        Attachment attachment = new Attachment();
        attachment.setFileName(file.getOriginalFilename());
        attachment.setFileType(Optional.ofNullable(file.getContentType()).orElse("application/octet-stream"));
        attachment.setFileUrl(fileUrl);
        attachment.setCloudinaryPublicId(publicId);
        attachment.setFileSize(file.getSize());
        attachment.setUploadedBy(uploadedBy);
        attachment.setTask(task);

        return attachmentRepository.save(attachment);
    }


    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_MIME_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: PDF, PNG, JPEG.");
        }

        if (file.getSize() > maxFileSizeBytes) {
            throw new IllegalArgumentException("File size exceeds maximum allowed: " + maxFileSizeBytes + " bytes");
        }
    }

    public Attachment getAttachmentById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id " + id));
    }

    public List<Attachment> getAttachmentsByTask(UUID taskId) {
        return attachmentRepository.findByTask_Id(taskId);
    }

    public java.io.InputStream getFileStream(Long id) throws IOException {
        Attachment attachment = getAttachmentById(id);
        String fileUrl = attachment.getFileUrl();

        if (fileUrl == null || fileUrl.isEmpty()) {
            throw new RuntimeException("File URL not found for attachment id: " + id);
        }

        // Open stream to Cloudinary URL (more efficient for large files)
        java.net.URL url = new java.net.URL(fileUrl);
        return url.openStream();
    }

    public byte[] downloadFile(Long id) throws IOException {
        try (java.io.InputStream inputStream = getFileStream(id)) {
            return inputStream.readAllBytes();
        }
    }

    public void deleteAttachment(Long id) {
        Attachment attachment = getAttachmentById(id);

        // Delete from Cloudinary if public_id exists
        if (attachment.getCloudinaryPublicId() != null && !attachment.getCloudinaryPublicId().isEmpty()) {
            try {
                // Determine resource type based on file type
                String resourceType = "raw";
                if (attachment.getFileType() != null) {
                    if (attachment.getFileType().startsWith("image/")) {
                        resourceType = "image";
                    } else if (attachment.getFileType().startsWith("video/")) {
                        resourceType = "video";
                    }
                }

                cloudinary.uploader().destroy(
                        attachment.getCloudinaryPublicId(),
                        ObjectUtils.asMap("resource_type", resourceType));
            } catch (IOException e) {
                System.err.println("Warning: Failed to delete file from Cloudinary: " + e.getMessage());
            }
        }

        // Delete from database
        attachmentRepository.delete(attachment);
    }
}