package com.collabspace.collabspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TeamMemberDto {
    @NotNull
    private UUID member_id;
    @NotNull
    private String fullName;
    @NotNull
    private String email;
    @NotNull
    private UUID project_id;
}
