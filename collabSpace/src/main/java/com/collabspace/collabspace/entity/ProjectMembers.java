package com.collabspace.collabspace.entity;

import com.collabspace.collabspace.dto.TeamMemberDto;
import com.collabspace.collabspace.enums.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "project_members")
public class ProjectMembers {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "project_id", nullable = false)
    private UUID projectId;
    @Column(name = "member_id",  nullable = false)
    private UUID memberId;
    @Column(name = "fullname",  nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String email;
    @Column(name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole =  MemberRole.MEMBER;
    @Column(name = "joined_at")
    private LocalDateTime joinedAt= LocalDateTime.now();
}


