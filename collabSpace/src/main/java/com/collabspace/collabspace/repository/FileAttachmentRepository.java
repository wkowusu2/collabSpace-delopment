package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileAttachmentRepository extends JpaRepository<Attachment, Long> {
}
