package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.entity.ProjectMembers;
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
    ProjectMembers findByProjectIdAndMemberId(@Param("projectId") UUID projectId,@Param("memberId") UUID memberId);

    List<ProjectMembers> findByProjectId(UUID projectId);

    @Query("""
        SELECT p 
        FROM Project p 
        JOIN ProjectMembers pm ON p.id = pm.projectId 
        WHERE pm.memberId = :memberId
    """)
    List<Project> findAllByMemberId(@Param("memberId") UUID memberId);
}
