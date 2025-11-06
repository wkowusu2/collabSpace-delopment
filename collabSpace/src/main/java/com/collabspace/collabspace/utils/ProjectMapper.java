package com.collabspace.collabspace.utils;

import com.collabspace.collabspace.dto.ProjectResponseDto;
import com.collabspace.collabspace.entity.Project;

public class ProjectMapper {

    private ProjectMapper() {
        // prevent instantiation
    }

    public static ProjectResponseDto toResponseDto(Project savedProject) {
        ProjectResponseDto responseDto = new ProjectResponseDto();
        responseDto.setId(savedProject.getId());
        responseDto.setName(savedProject.getName());
        responseDto.setDescription(savedProject.getDescription());
        responseDto.setCreatedBy(savedProject.getCreatedBy());
        responseDto.setStartDate(savedProject.getStartDate());
        responseDto.setEndDate(savedProject.getEndDate());
        responseDto.setStatus(savedProject.getStatus());
        return responseDto;
    }
}
