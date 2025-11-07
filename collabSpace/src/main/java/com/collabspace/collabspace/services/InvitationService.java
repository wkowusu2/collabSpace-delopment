package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.InvitationDto;
import com.collabspace.collabspace.dto.MemberMapDto;
import com.collabspace.collabspace.entity.Invitation;
import com.collabspace.collabspace.entity.ProjectMembers;
import com.collabspace.collabspace.enums.InvitationStatus;
import com.collabspace.collabspace.enums.MemberRole;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.repository.InvitationRepository;
import com.collabspace.collabspace.repository.ProjectMembersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class InvitationService {
    private final InvitationRepository invitationRepository;
    private final ProjectMembersRepository projectMembersRepository;

    @Transactional
    public InvitationDto createInvitation(UUID projectId, String email, String fullName, UUID inviterId) {
        // Check if invitation already exists
        var existingInvitation = invitationRepository.findByProjectIdAndEmail(projectId, email);

        if (existingInvitation.isPresent()) {
            Invitation inv = existingInvitation.get();
            if (inv.getInvitationStatus() == InvitationStatus.PENDING) {
                throw new IllegalArgumentException("Invitation already sent to this email for this project");
            }
        }

        Invitation invitation = new Invitation();
        invitation.setProjectId(projectId);
        invitation.setEmail(email);
        invitation.setFullName(fullName);
        invitation.setInviterId(inviterId);
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setCreatedAt(LocalDateTime.now());

        Invitation savedInvitation = invitationRepository.save(invitation);
        return convertToDto(savedInvitation);
    }

    @Transactional
    public MemberMapDto acceptInvitation(UUID invitationId, UUID memberId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ProjectDoesNotExistException("Invitation not found"));

        if (invitation.getInvitationStatus() != InvitationStatus.PENDING) {
            throw new IllegalArgumentException(
                    "This invitation has already been " + invitation.getInvitationStatus().toString().toLowerCase());
        }

        // Mark invitation as accepted
        invitation.setInvitationStatus(InvitationStatus.ACCEPTED);
        invitation.setAcceptedAt(LocalDateTime.now());
        invitation.setMemberId(memberId);
        invitationRepository.save(invitation);

        // Create ProjectMembers record
        ProjectMembers projectMember = new ProjectMembers();
        projectMember.setProjectId(invitation.getProjectId());
        projectMember.setMemberId(memberId);
        projectMember.setEmail(invitation.getEmail());
        projectMember.setFullName(invitation.getFullName());
        projectMember.setMemberRole(MemberRole.MEMBER);
        projectMember.setJoinedAt(LocalDateTime.now());

        projectMembersRepository.save(projectMember);

        MemberMapDto response = new MemberMapDto();
        response.setMessage(invitation.getFullName() + " has accepted the invitation and joined the project");
        response.setStatus(true);
        return response;
    }

    @Transactional
    public MemberMapDto rejectInvitation(UUID invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ProjectDoesNotExistException("Invitation not found"));

        if (invitation.getInvitationStatus() != InvitationStatus.PENDING) {
            throw new IllegalArgumentException("This invitation cannot be rejected as it is already "
                    + invitation.getInvitationStatus().toString().toLowerCase());
        }

        invitation.setInvitationStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);

        MemberMapDto response = new MemberMapDto();
        response.setMessage("Invitation rejected successfully");
        response.setStatus(true);
        return response;
    }

    public List<InvitationDto> getInvitationsByEmail(String email, InvitationStatus status) {
        List<Invitation> invitations = invitationRepository.findAllByEmailAndStatus(email, status);
        return invitations.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<InvitationDto> getPendingInvitationsByEmail(String email) {
        return getInvitationsByEmail(email, InvitationStatus.PENDING);
    }

    public InvitationDto getInvitationById(UUID invitationId) {
        Invitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ProjectDoesNotExistException("Invitation not found"));
        return convertToDto(invitation);
    }

    private InvitationDto convertToDto(Invitation invitation) {
        InvitationDto dto = new InvitationDto();
        dto.setId(invitation.getId());
        dto.setProjectId(invitation.getProjectId());
        dto.setMemberId(invitation.getMemberId());
        dto.setInviterId(invitation.getInviterId());
        dto.setEmail(invitation.getEmail());
        dto.setInvitationStatus(invitation.getInvitationStatus().toString());
        dto.setCreatedAt(invitation.getCreatedAt());
        dto.setAcceptedAt(invitation.getAcceptedAt());
        dto.setFullName(invitation.getFullName());
        return dto;
    }
}
