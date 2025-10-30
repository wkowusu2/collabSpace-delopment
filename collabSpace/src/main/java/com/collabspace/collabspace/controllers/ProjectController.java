package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.ProjectRequestDto;
import com.collabspace.collabspace.dto.ProjectResponseDto;
import com.collabspace.collabspace.services.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponseDto>  createProject(@Valid @RequestBody ProjectRequestDto projectDto,
                        @RequestHeader("X-User-Id") UUID userId)
     {
        ProjectResponseDto responseDto = projectService.createProject(projectDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }
}
