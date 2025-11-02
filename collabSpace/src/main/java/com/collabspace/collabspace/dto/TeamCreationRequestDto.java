package com.collabspace.collabspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class TeamCreationRequestDto {
    @NotNull
    private UUID projectId;
    @NotNull
    private String teamName;
    private List<UUID> memberIds;
}
