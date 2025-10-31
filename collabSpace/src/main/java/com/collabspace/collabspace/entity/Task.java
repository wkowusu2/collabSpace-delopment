package com.collabspace.collabspace.entity;

import com.collabspace.collabspace.enums.Priority;
import com.collabspace.collabspace.enums.TastStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @NotNull
    private String title;
    private String description;
    @Column(name = "assignee_id")
    private UUID assigneeId;
    @Enumerated(EnumType.STRING)
    private TastStatus status;
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM;
    @Column(name = "due_date")
    private Date dueDate;
    @Column(name = "created_at")
    private LocalDate creationAt=LocalDate.now();
}
