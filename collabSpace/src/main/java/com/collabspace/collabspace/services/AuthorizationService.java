package com.collabspace.collabspace.services;

import com.collabspace.collabspace.entity.Attachment;
import com.collabspace.collabspace.entity.Task;
import com.collabspace.collabspace.enums.MemberRole;
import com.collabspace.collabspace.repository.AttachmentRepository;
import com.collabspace.collabspace.repository.ProjectMembersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final AttachmentRepository attachmentRepository;
    private final ProjectMembersRepository projectMembersRepository;

    public boolean canDeleteAttachment(UUID userId, Long attachmentId) {
        Attachment attachment = attachmentRepository.findById(attachmentId)
                .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));

        if (attachment.getUploadedBy() != null && attachment.getUploadedBy().equals(userId)) {
            return true;
        }

        if (attachment.getTask() == null) {
            return false;
        }

        Task task = attachment.getTask();
        if (task.getProject() == null) {
            return false;
        }

        UUID projectId = task.getProject().getId();

        return projectMembersRepository.existsByProjectIdAndMemberIdAndMemberRoleIn(
                projectId,
                userId,
                java.util.List.of(MemberRole.PROJECT_ADMIN, MemberRole.PROJECT_MANAGER));
    }
}