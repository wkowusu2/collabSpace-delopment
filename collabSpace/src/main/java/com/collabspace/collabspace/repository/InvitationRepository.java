package com.collabspace.collabspace.repository;

import com.collabspace.collabspace.entity.Invitation;
import com.collabspace.collabspace.enums.InvitationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, UUID> {

    @Query("""
                SELECT i
                FROM Invitation i
                WHERE i.email = :email AND i.invitationStatus = :status
            """)
    List<Invitation> findByEmailAndStatus(
            @Param("email") String email,
            @Param("status") InvitationStatus status);

    @Query("""
                SELECT i
                FROM Invitation i
                WHERE i.projectId = :projectId AND i.email = :email
            """)
    Optional<Invitation> findByProjectIdAndEmail(
            @Param("projectId") UUID projectId,
            @Param("email") String email);

    @Query("""
                SELECT i
                FROM Invitation i
                WHERE i.projectId = :projectId AND i.invitationStatus = :status
            """)
    List<Invitation> findByProjectIdAndStatus(
            @Param("projectId") UUID projectId,
            @Param("status") InvitationStatus status);

    @Query("""
                SELECT i
                FROM Invitation i
                WHERE i.email = :email AND i.invitationStatus = :status
            """)
    List<Invitation> findAllByEmailAndStatus(
            @Param("email") String email,
            @Param("status") InvitationStatus status);
}
