package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.ProjectStatsDto;
import com.collabspace.collabspace.services.ProjectAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class ProjectAnalyticsController {

    private final ProjectAnalyticsService projectAnalyticsService;

    @GetMapping("/projects/{projectId}")
    public ResponseEntity<ProjectStatsDto> getProjectStatistics(@PathVariable UUID projectId) {
        ProjectStatsDto stats = projectAnalyticsService.getProjectStatistics(projectId);
        return ResponseEntity.ok(stats);
    }
}