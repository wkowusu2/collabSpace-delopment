package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.ProjectStatsDto;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.repository.ProjectRepository;
import com.collabspace.collabspace.repository.TaskAnalyticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProjectAnalyticsService {

    private final TaskAnalyticsRepository taskAnalyticsRepository;
    private final ProjectRepository projectRepository;

    public ProjectStatsDto getProjectStatistics(UUID projectId) {
        // Verify project exists
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ProjectDoesNotExistException("Project not found with id: " + projectId));

        ProjectStatsDto stats = new ProjectStatsDto();
        stats.setProjectId(projectId);
        stats.setProjectName(project.getName());

        // Get total and completed tasks
        Long totalTasks = taskAnalyticsRepository.countTotalTasksByProject(projectId);
        Long completedTasks = taskAnalyticsRepository.countCompletedTasksByProject(projectId);

        stats.setTotalTasks(totalTasks != null ? totalTasks.intValue() : 0);
        stats.setCompletedTasks(completedTasks != null ? completedTasks.intValue() : 0);

        // Calculate completion rate
        double completionRate = totalTasks != null && totalTasks > 0 ?
                (completedTasks != null ? (double) completedTasks / totalTasks * 100 : 0) : 0;
        stats.setCompletionRate(Math.round(completionRate * 100.0) / 100.0);

        // Get tasks by status
        Map<String, Long> tasksByStatus = new HashMap<>();
        List<Object[]> statusCounts = taskAnalyticsRepository.countTasksByStatus(projectId);
        for (Object[] result : statusCounts) {
            tasksByStatus.put(result[0].toString(), (Long) result[1]);
        }
        stats.setTasksByStatus(tasksByStatus);

        // Get tasks by assignee
        Map<UUID, Long> tasksByAssignee = new HashMap<>();
        List<Object[]> assigneeCounts = taskAnalyticsRepository.countTasksByAssignee(projectId);
        for (Object[] result : assigneeCounts) {
            tasksByAssignee.put((UUID) result[0], (Long) result[1]);
        }
        stats.setTasksByAssignee(tasksByAssignee);

        // Get tasks by priority
        Map<String, Long> tasksByPriority = new HashMap<>();
        List<Object[]> priorityCounts = taskAnalyticsRepository.countTasksByPriority(projectId);
        for (Object[] result : priorityCounts) {
            tasksByPriority.put(result[0].toString(), (Long) result[1]);
        }
        stats.setTasksByPriority(tasksByPriority);

        return stats;
    }
}