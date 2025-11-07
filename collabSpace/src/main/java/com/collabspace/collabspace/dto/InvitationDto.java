package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class InvitationDto {
    private UUID id;
    private UUID projectId;
    private UUID memberId;
    private UUID inviterId;
    private String email;
    private String invitationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime acceptedAt;
    private String fullName;
}
