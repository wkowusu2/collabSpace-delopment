package com.collabspace.collabspace.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDetailsDto {
    private UUID id;
    private String fullName;
    private String email;
}
