package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface TaskAnalyticsRepository extends JpaRepository<Task, UUID> {

    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.project.id = :projectId GROUP BY t.status")
    List<Object[]> countTasksByStatus(@Param("projectId") UUID projectId);

    @Query("SELECT t.assigneeId, COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.assigneeId IS NOT NULL GROUP BY t.assigneeId")
    List<Object[]> countTasksByAssignee(@Param("projectId") UUID projectId);

    @Query("SELECT t.priority, COUNT(t) FROM Task t WHERE t.project.id = :projectId GROUP BY t.priority")
    List<Object[]> countTasksByPriority(@Param("projectId") UUID projectId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.id = :projectId")
    Long countTotalTasksByProject(@Param("projectId") UUID projectId);

    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.status = 'DONE'")
    Long countCompletedTasksByProject(@Param("projectId") UUID projectId);
}