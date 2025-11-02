package com.collabspace.collabspace.services;

import com.collabspace.collabspace.dto.TeamCreationRequestDto;
import com.collabspace.collabspace.dto.TeamResponseDto;
import com.collabspace.collabspace.dto.UserDetailsDto;
import com.collabspace.collabspace.entity.Project;
import com.collabspace.collabspace.entity.Team;
import com.collabspace.collabspace.exceptions.MemberAlreadyAddedToProjectException;
import com.collabspace.collabspace.exceptions.MemberIsNotInTheTeamException;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.exceptions.TeamDoesNotExistException;
import com.collabspace.collabspace.repository.ProjectRepository;
import com.collabspace.collabspace.repository.TeamRepository;
import com.collabspace.collabspace.utils.TeamMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TeamService {
    private TeamRepository teamRepository;
    private ProjectRepository projectRepository;

    @Transactional
    public Team createTeam(UserDetailsDto userDetails, TeamCreationRequestDto teamCreationRequestDto) {
        //check if the project exist by id
        Project project = projectRepository.findById(teamCreationRequestDto.getProjectId())
                .orElseThrow(() -> new ProjectDoesNotExistException("Project not found"));
        //make a team
        Team team = new Team();
        team.setName(teamCreationRequestDto.getTeamName());
        team.setMemberIds(teamCreationRequestDto.getMemberIds() != null
                ? teamCreationRequestDto.getMemberIds()
                : new ArrayList<>());

        team.setCreatedBy(userDetails.getId());
        team.setProject(project);
        return teamRepository.save(team);
    }

    public TeamResponseDto getTeam(UUID teamId) {
         Team team = teamRepository.findById(teamId).orElseThrow(
                () -> new TeamDoesNotExistException("Team does not exist")
        );
        return TeamMapper.toTeamResponse(team);
    }

    @Transactional
    public Map<String, String> deleteTeam(UUID teamId) {
        //check if team exists
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->  new TeamDoesNotExistException("Team does not exist"));
        teamRepository.delete(team);
        Map<String, String> response = new HashMap<>();
        String status = "true";
        String message = "Team has been deleted";
        response.put("status", status);
        response.put("message", message);
        return response;
    }

    @Transactional
    public Map<String, String> addMemberToTeam(UUID teamId, UUID memberId) {
        //check if the team exit by Id
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamDoesNotExistException("Team does not exist"));
        //check if member is in the team
        boolean memberInTeam = team.getMemberIds().contains(memberId);
        if(memberInTeam) {
            throw new MemberAlreadyAddedToProjectException("Member already in team");
        }
        team.getMemberIds().add(memberId);
        teamRepository.save(team);
        Map<String, String> response = new HashMap<>();
        String status = "true";
        String message = "Member has been added";
        response.put("status", status);
        response.put("message", message);
        return response;
    }

    @Transactional
    public Map<String, String> removeMemberFromTeam(UUID teamId, UUID memberId) {
        //check if the team exit by Id
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new TeamDoesNotExistException("Team does not exist"));
        //check if member is in the team
        boolean memberInTeam = team.getMemberIds().contains(memberId);
        if(!memberInTeam) {
            throw new MemberIsNotInTheTeamException("Member is not part of the team");
        }
        team.getMemberIds().remove(memberId);
        teamRepository.save(team);
        Map<String, String> response = new HashMap<>();
        String status = "true";
        String message = "Member has been removed";
        response.put("status", status);
        response.put("message", message);
        return response;
    }
}
