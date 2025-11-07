package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
public class ProjectStatsDto {
    private UUID projectId;
    private String projectName;
    private int totalTasks;
    private int completedTasks;
    private double completionRate;
    private Map<String, Long> tasksByStatus;
    private Map<UUID, Long> tasksByAssignee;
    private Map<String, Long> tasksByPriority;
}