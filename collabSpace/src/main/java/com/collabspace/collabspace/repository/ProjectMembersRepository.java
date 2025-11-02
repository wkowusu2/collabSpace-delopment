package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.ProjectMembers;
import com.collabspace.collabspace.enums.MemberRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProjectMembersRepository extends JpaRepository<ProjectMembers, UUID> {
    @Query(value = """
            select *
                    from project_members
                    where project_id= :projectId and member_id = :memberId
            """, nativeQuery = true)
    ProjectMembers findByProjectIdAndMemberId(@Param("projectId") UUID projectId, @Param("memberId") UUID memberId);

    List<ProjectMembers> findByProjectId(UUID projectId);

    boolean existsByProjectIdAndMemberIdAndMemberRoleIn(
            UUID projectId,
            UUID memberId,
            List<MemberRole> memberRoles);
}