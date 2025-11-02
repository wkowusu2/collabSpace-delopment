package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.RoleChangeRequest;
import com.collabspace.collabspace.dto.TeamMemberDto;
import com.collabspace.collabspace.entity.ProjectMembers;
import com.collabspace.collabspace.enums.MemberRole;
import com.collabspace.collabspace.exceptions.MemberAlreadyAddedToProjectException;
import com.collabspace.collabspace.exceptions.MemberIsNotInTheTeamException;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.repository.ProjectMembersRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class ProjectMembersService {
    private final ProjectMembersRepository projectMembersRepository;

    @Transactional
    public Map<String, String> addMember(@Valid TeamMemberDto projectMembers) {
        //check if the member has already been added to the project
        ProjectMembers savedMember = projectMembersRepository.findByProjectIdAndMemberId(projectMembers.getProject_id(), projectMembers.getMember_id());
        if (savedMember != null) {
            throw new MemberAlreadyAddedToProjectException(projectMembers.getFullName()+" is already in "+ projectMembers.getProject_id());
        }
        ProjectMembers projectMembersEntity = new ProjectMembers();
        projectMembersEntity.setProjectId(projectMembers.getProject_id());
        projectMembersEntity.setMemberId(projectMembers.getMember_id());
        projectMembersEntity.setEmail(projectMembers.getEmail());
        projectMembersEntity.setFullName(projectMembers.getFullName());
        projectMembersRepository.save(projectMembersEntity);
        Map<String,String> map = new HashMap<>();
        String message = projectMembers.getFullName()+ " has been added to the project";
        map.put("success", "true");
        map.put("message", message);
        return map;
    }

    public List<UUID> getProjectMembers(UUID projectId) {
        // Fetch all members linked to the project
        List<ProjectMembers> foundMembers = projectMembersRepository.findByProjectId(projectId);

        // Extract just the member IDs
        return foundMembers.stream()
                .map(ProjectMembers::getMemberId)
                .toList();
    }


    @Transactional
    public Map<String, String> removeMemberFromTeam(UUID projectId, UUID memberId) {
        //check if the member is in the team if yes then remove them
        ProjectMembers savedMember = projectMembersRepository.findByProjectIdAndMemberId(projectId, memberId);
        if (savedMember == null) {
           throw new MemberIsNotInTheTeamException("The member is not in the team");
        }
        projectMembersRepository.delete(savedMember);
        Map<String,String> map = new HashMap<>();
        String message = "Member with id" + memberId + " has been removed from the team";
        map.put("success", "true");
        map.put("message", message);
        return map;
    }

    @Transactional
    public Map<String, String> updateMemberRole(UUID projectId, UUID memberId, RoleChangeRequest role) {
        ProjectMembers savedMember = projectMembersRepository.findByProjectIdAndMemberId(projectId,memberId);
        if (savedMember == null) {
            throw new MemberIsNotInTheTeamException("The member is not in the team");
        }
        savedMember.setMemberRole(role.getRole());
        projectMembersRepository.save(savedMember);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Member role updated successfully");
        response.put("memberId", memberId.toString());
        response.put("newRole", savedMember.getMemberRole().name());
        return response;
    }
}
