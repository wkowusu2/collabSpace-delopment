package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.CreateProjectDto;
import com.collabspace.collabspace.dto.ProjectRequestDto;
import com.collabspace.collabspace.dto.ProjectResponseDto;
import com.collabspace.collabspace.dto.UserDetailsDto;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.services.ProjectService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;


    @PostMapping
    public ResponseEntity<ProjectResponseDto>  createProject(@Valid @RequestBody CreateProjectDto projectDto,
                        @RequestHeader("X-User-Id") UUID creatorId) throws JsonProcessingException {

        ProjectResponseDto responseDto = projectService.createProject(projectDto, creatorId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto> getProjectById(@PathVariable UUID projectId){
        ProjectResponseDto projectResponseDto = projectService.getProjectById(projectId);
        return ResponseEntity.ok(projectResponseDto);
    }
    @GetMapping
    public ResponseEntity<List<ProjectResponseDto>> getAllProjectsForAMember(@RequestParam UUID memberId){
        List<ProjectResponseDto> response = projectService.getAllProjectsForMember(memberId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponseDto>  updateProject(@Valid @PathVariable UUID projectId, @RequestBody ProjectRequestDto projectDto,
                                                             @RequestHeader("X-User-Id") UUID userId)
    {
        ProjectResponseDto responseDto = projectService.updateProject(projectId,projectDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Map<String, String>> deleteProject(@PathVariable UUID projectId){
        Map<String, String> responseDto = projectService.deleteProject(projectId);
        return ResponseEntity.ok(responseDto);
    }
}
