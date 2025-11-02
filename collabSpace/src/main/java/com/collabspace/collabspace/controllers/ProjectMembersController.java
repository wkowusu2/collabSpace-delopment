package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.RoleChangeRequest;
import com.collabspace.collabspace.dto.TeamMemberDto;
import com.collabspace.collabspace.entity.ProjectMembers;
import com.collabspace.collabspace.services.ProjectMembersService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/project-members")
@AllArgsConstructor
public class ProjectMembersController {
    private final ProjectMembersService  projectMembersService;
    @PostMapping
    public ResponseEntity<?> addProjectMembers(@Valid @RequestBody TeamMemberDto projectMembers) {
       Map<String, String> response = projectMembersService.addMember(projectMembers);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<?> getProjectMembers(@PathVariable UUID projectId) {
        List<UUID> allMembers = projectMembersService.getProjectMembers(projectId);
        return ResponseEntity.ok(allMembers);
    }

    @DeleteMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable UUID projectId, @PathVariable UUID memberId) {
        Map<String,String> response = projectMembersService.removeMemberFromTeam(projectId, memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}/members/{memberId}")
    public ResponseEntity<?> updateRoleOfMembers(@PathVariable UUID projectId, @PathVariable UUID memberId, @Valid @RequestBody RoleChangeRequest role) {
        Map<String,String> response = projectMembersService.updateMemberRole(projectId, memberId, role);
        return ResponseEntity.ok(response);
    }
}
