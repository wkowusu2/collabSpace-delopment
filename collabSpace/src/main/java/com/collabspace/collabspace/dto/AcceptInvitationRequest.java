package com.collabspace.collabspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AcceptInvitationRequest {
    @NotNull(message = "Invitation ID is required")
    private UUID invitationId;
}
