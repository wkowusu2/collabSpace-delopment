package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateMemberRoleDto {
    private boolean status;
    private String message;
    private UUID memberId;
    private String newRole;
}
