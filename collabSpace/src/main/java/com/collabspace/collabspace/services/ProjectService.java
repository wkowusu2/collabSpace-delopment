package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.ProjectRequestDto;
import com.collabspace.collabspace.dto.ProjectResponseDto;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.entity.Team;
import com.collabspace.collabspace.repository.ProjectRepository;
import com.collabspace.collabspace.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TeamRepository teamRepository;
    public ProjectService(ProjectRepository projectRepository, TeamRepository teamRepository) {
        this.projectRepository = projectRepository;
        this.teamRepository = teamRepository;
    }

    @Transactional
    public ProjectResponseDto createProject(ProjectRequestDto projectRequestDto, UUID createdBy) {
        //create a project
        Project project = new Project();
        project.setName(projectRequestDto.getName());
        project.setDescription(projectRequestDto.getDescription());
        project.setCreatedBy(createdBy);
        project.setStartDate(projectRequestDto.getStart_date());
        project.setEndDate(projectRequestDto.getEnd_date());

        //create the team
        Team team = new Team();
        team.setName(projectRequestDto.getName() + " Team");
        team.setCreatedBy(createdBy);
        Team savedTeam = teamRepository.save(team);
        project.setTeam(savedTeam);
        Project savedProject = projectRepository.save(project);
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(savedProject.getId());
        responseDto.setName(savedProject.getName());
        responseDto.setDescription(savedProject.getDescription());
        responseDto.setCreatedBy(savedProject.getCreatedBy());
        responseDto.setStartDate(savedProject.getStartDate());
        responseDto.setEndDate(savedProject.getEndDate());
        responseDto.setTeamId(savedProject.getTeam().getId());
        responseDto.setStatus(savedProject.getStatus());
        return responseDto;
    }
}
