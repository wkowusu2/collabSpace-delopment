package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.CreateProjectDto;
import com.collabspace.collabspace.dto.ProjectRequestDto;
import com.collabspace.collabspace.dto.ProjectResponseDto;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.entity.ProjectMembers;
import com.collabspace.collabspace.enums.MemberRole;
import com.collabspace.collabspace.repository.ProjectMembersRepository;
import com.collabspace.collabspace.repository.ProjectRepository;
import com.collabspace.collabspace.utils.ProjectMapper;
import com.collabspace.collabspace.utils.ProjectValidator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Service
@AllArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectMembersRepository projectMembersRepository;
    private final ProjectValidator projectValidator;


    @Transactional
    public ProjectResponseDto createProject(CreateProjectDto projectRequestDto, UUID creatorId) {
        //create a project
        Project project = new Project();
        project.setName(projectRequestDto.getName());
        project.setDescription(projectRequestDto.getDescription());
        project.setCreatedBy(creatorId);
        project.setStartDate(projectRequestDto.getStart_date());
        project.setEndDate(projectRequestDto.getEnd_date());
        Project savedProject = projectRepository.save(project);

        //Add creator to the project as member with Admin role
        ProjectMembers member = new ProjectMembers();
        member.setMemberRole(MemberRole.PROJECT_ADMIN);
        member.setProjectId(project.getId());
        member.setMemberId(creatorId);
        member.setEmail(projectRequestDto.getEmail());
        member.setFullName(projectRequestDto.getFullName());
        projectMembersRepository.save(member);
        return ProjectMapper.toResponseDto(savedProject);
    }

    @Transactional
    public ProjectResponseDto updateProject(UUID projectId,ProjectRequestDto projectRequestDto, UUID createdBy) {
        Project project = projectValidator.ensureProjectExists(projectId);
        project.setName(projectRequestDto.getName());
        project.setDescription(projectRequestDto.getDescription());
        project.setStartDate(projectRequestDto.getStart_date());
        project.setEndDate(projectRequestDto.getEnd_date());
        Project savedProject = projectRepository.save(project);
        return ProjectMapper.toResponseDto(savedProject);

    }


    public ProjectResponseDto getProjectById(UUID projectId) {
        Project project = projectValidator.ensureProjectExists(projectId);
        return ProjectMapper.toResponseDto(project);

    }

    public Map<String, String> deleteProject(UUID projectId) {
        Project project = projectValidator.ensureProjectExists(projectId);
        projectRepository.delete(project);
        Map<String, String> responseDto = new HashMap<>();
        responseDto.put("status", "success");
        responseDto.put("message", project.getName() +" has been deleted");
        return responseDto;
    }

    public List<ProjectResponseDto> getAllProjectsForMember(UUID memberId) {
        List<Project> projects = projectMembersRepository.findAllByMemberId(memberId);
        return projects.stream()
                .map(ProjectMapper::toResponseDto)
                .toList();
    }
}
