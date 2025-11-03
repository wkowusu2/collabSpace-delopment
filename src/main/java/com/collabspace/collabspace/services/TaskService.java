package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.*;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.entity.Task;
import com.collabspace.collabspace.entity.Subtask;
import com.collabspace.collabspace.enums.TaskStatus;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.exceptions.TaskNotFoundException;
import com.collabspace.collabspace.exceptions.InvalidStatusException;
import com.collabspace.collabspace.repository.ProjectRepository;
import com.collabspace.collabspace.repository.TaskRepository;
import com.collabspace.collabspace.repository.SubtaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public TaskResponseDto createTask(TaskRequestDto taskRequestDto, UUID userId) {
        Project project = projectRepository.findById(taskRequestDto.getProjectId())
                .orElseThrow(() -> new ProjectDoesNotExistException("Project not found with id: " + taskRequestDto.getProjectId()));

        Task task = new Task();
        task.setTitle(taskRequestDto.getTitle());
        task.setDescription(taskRequestDto.getDescription());
        task.setProject(project);
        task.setAssigneeId(taskRequestDto.getAssigneeId());
        task.setDueDate(taskRequestDto.getDueDate());
        task.setPriority(taskRequestDto.getPriority());
        task.setStatus(TaskStatus.TO_DO);

        Task savedTask = taskRepository.save(task);
        return convertToTaskResponseDto(savedTask);
    }

    public TaskResponseDto getTaskById(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        return convertToTaskResponseDto(task);
    }

    public List<TaskResponseDto> getAllTasksByProject(UUID projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new ProjectDoesNotExistException("Project not found with id: " + projectId);
        }

        List<Task> tasks = taskRepository.findByProjectId(projectId);
        return tasks.stream()
                .map(this::convertToTaskResponseDto)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDto> getTasksByAssignee(UUID assigneeId) {
        List<Task> tasks = taskRepository.findByAssigneeId(assigneeId);
        return tasks.stream()
                .map(this::convertToTaskResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public TaskResponseDto updateTask(UUID taskId, TaskRequestDto taskRequestDto, UUID userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        if (taskRequestDto.getProjectId() != null) {
            Project project = projectRepository.findById(taskRequestDto.getProjectId())
                    .orElseThrow(() -> new ProjectDoesNotExistException("Project not found with id: " + taskRequestDto.getProjectId()));
            task.setProject(project);
        }

        if (taskRequestDto.getTitle() != null) {
            task.setTitle(taskRequestDto.getTitle());
        }
        if (taskRequestDto.getDescription() != null) {
            task.setDescription(taskRequestDto.getDescription());
        }
        if (taskRequestDto.getAssigneeId() != null) {
            task.setAssigneeId(taskRequestDto.getAssigneeId());
        }
        if (taskRequestDto.getDueDate() != null) {
            task.setDueDate(taskRequestDto.getDueDate());
        }
        if (taskRequestDto.getPriority() != null) {
            task.setPriority(taskRequestDto.getPriority());
        }

        Task updatedTask = taskRepository.save(task);
        return convertToTaskResponseDto(updatedTask);
    }

    @Transactional
    public TaskResponseDto updateTaskStatus(UUID taskId, String status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            task.setStatus(taskStatus);
            Task updatedTask = taskRepository.save(task);
            return convertToTaskResponseDto(updatedTask);
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException("Invalid status: " + status + ". Valid statuses are: TO_DO, IN_PROGRESS, DONE, CANCELLED");
        }
    }

    @Transactional
    public TaskResponseDto updateTaskAssignee(UUID taskId, UUID assigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        task.setAssigneeId(assigneeId);
        Task updatedTask = taskRepository.save(task);
        return convertToTaskResponseDto(updatedTask);
    }

    @Transactional
    public void deleteTask(UUID taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
        taskRepository.delete(task);
    }

    @Transactional
    public SubtaskResponseDto addSubtask(UUID taskId, SubtaskRequestDto subtaskRequestDto) {
        Task parentTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Parent task not found with id: " + taskId));

        Subtask subtask = new Subtask();
        subtask.setTitle(subtaskRequestDto.getTitle());
        subtask.setDescription(subtaskRequestDto.getDescription());
        subtask.setParentTask(parentTask);
        subtask.setAssigneeId(subtaskRequestDto.getAssigneeId());
        subtask.setStatus(TaskStatus.TO_DO);

        Subtask savedSubtask = subtaskRepository.save(subtask);
        return convertToSubtaskResponseDto(savedSubtask);
    }

    @Transactional
    public TaskResponseDto addLinkedWorkItem(UUID taskId, UUID linkedTaskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));

        Task linkedTask = taskRepository.findById(linkedTaskId)
                .orElseThrow(() -> new TaskNotFoundException("Linked task not found with id: " + linkedTaskId));

        if (!task.getLinkedWorkItems().contains(linkedTask)) {
            task.getLinkedWorkItems().add(linkedTask);
        }

        Task updatedTask = taskRepository.save(task);
        return convertToTaskResponseDto(updatedTask);
    }

    public List<SubtaskResponseDto> getSubtasksByTask(UUID taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException("Task not found with id: " + taskId);
        }

        List<Subtask> subtasks = subtaskRepository.findByParentTaskId(taskId);
        return subtasks.stream()
                .map(this::convertToSubtaskResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto convertToTaskResponseDto(Task task) {
        TaskResponseDto responseDto = new TaskResponseDto();
        responseDto.setId(task.getId());
        responseDto.setTitle(task.getTitle());
        responseDto.setDescription(task.getDescription());
        responseDto.setProjectId(task.getProject().getId());
        responseDto.setProjectName(task.getProject().getName());
        responseDto.setAssigneeId(task.getAssigneeId());
        responseDto.setStatus(task.getStatus());
        responseDto.setPriority(task.getPriority());
        responseDto.setDueDate(task.getDueDate());
        responseDto.setCreatedAt(task.getCreationAt());

        List<SubtaskResponseDto> subtaskDtos = subtaskRepository.findByParentTaskId(task.getId())
                .stream()
                .map(this::convertToSubtaskResponseDto)
                .collect(Collectors.toList());
        responseDto.setSubtasks(subtaskDtos);

        if (task.getLinkedWorkItems() != null) {
            List<TaskLinkDto> linkedWorkItemDtos = task.getLinkedWorkItems().stream()
                    .map(linkedTask -> {
                        TaskLinkDto linkDto = new TaskLinkDto();
                        linkDto.setId(linkedTask.getId());
                        linkDto.setTitle(linkedTask.getTitle());
                        linkDto.setStatus(linkedTask.getStatus().name());
                        return linkDto;
                    })
                    .collect(Collectors.toList());
            responseDto.setLinkedWorkItems(linkedWorkItemDtos);
        }

        return responseDto;
    }

    private SubtaskResponseDto convertToSubtaskResponseDto(Subtask subtask) {
        SubtaskResponseDto responseDto = new SubtaskResponseDto();
        responseDto.setId(subtask.getId());
        responseDto.setTitle(subtask.getTitle());
        responseDto.setDescription(subtask.getDescription());
        responseDto.setParentTaskId(subtask.getParentTask().getId());
        responseDto.setAssigneeId(subtask.getAssigneeId());
        responseDto.setStatus(subtask.getStatus());
        responseDto.setCreatedAt(subtask.getCreatedAt());
        return responseDto;
    }
}