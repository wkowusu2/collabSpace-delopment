package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    // NOTE: uploadedBy is UUID in the updated Attachment entity
    List<Attachment> findByUploadedBy(UUID uploadedBy);

    List<Attachment> findByFileType(String fileType);

    // Find attachments that belong to a specific task (task_id FK)
    List<Attachment> findByTask_Id(UUID taskId);
}
