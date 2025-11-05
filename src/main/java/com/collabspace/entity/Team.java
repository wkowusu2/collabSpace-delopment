package com.collabspace.collabspace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "teams")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
    @Column(nullable = false)
    private String name;
    @ElementCollection
    @CollectionTable(
            name = "team_member_ids",
            joinColumns = @JoinColumn(name = "team_id")
    )
    @Column(name = "member_id")
    private List<UUID> memberIds = new ArrayList<>();

    @Column(name = "created_by")
    private UUID createdBy;
    private Instant createdAt =  Instant.now();
}
