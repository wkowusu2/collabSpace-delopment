package com.collabspace.collabspace.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusCountDto {
    private String status;
    private Long count;

    public TaskStatusCountDto(String status, Long count) {
        this.status = status;
        this.count = count;
    }
}