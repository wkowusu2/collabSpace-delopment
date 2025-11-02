package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TeamResponseDto {
    private UUID teamId;
    private String name;
    private UUID createdBy;
    private UUID projectId;
    private List<UUID> memberIds;
}
