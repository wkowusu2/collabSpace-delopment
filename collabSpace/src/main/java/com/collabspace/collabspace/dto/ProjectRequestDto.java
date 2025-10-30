package com.collabspace.collabspace.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class ProjectRequestDto {
    @NotNull(message = "Name field should not be empty")
    private String name;
    @NotNull(message = "Description field should not be empty")
    private String description;
    @NotNull(message = "Start date field should not be empty")
    private LocalDate start_date;
    @NotNull(message = "End date field should not be empty")
    private LocalDate end_date;
}
