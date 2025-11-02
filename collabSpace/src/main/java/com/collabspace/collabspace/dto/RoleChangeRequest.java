package com.collabspace.collabspace.dto;

import com.collabspace.collabspace.enums.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleChangeRequest {
    @NotNull
    MemberRole role;
}
