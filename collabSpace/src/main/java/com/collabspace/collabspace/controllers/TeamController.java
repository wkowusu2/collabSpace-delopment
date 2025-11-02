package com.collabspace.collabspace.controllers;

import com.collabspace.collabspace.dto.TeamCreationRequestDto;
import com.collabspace.collabspace.dto.TeamResponseDto;
import com.collabspace.collabspace.dto.UserDetailsDto;
import com.collabspace.collabspace.entity.Team;
import com.collabspace.collabspace.services.TeamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/team")
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestHeader("X-User") String userJson,
                                           @Valid @RequestBody TeamCreationRequestDto teamCreationRequestDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        UserDetailsDto userDetails = mapper.readValue(userJson, UserDetailsDto.class);
        Team response = teamService.createTeam(userDetails, teamCreationRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable UUID teamId) {
        TeamResponseDto response = teamService.getTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<Map<String, String>> deleteTeam(@PathVariable UUID teamId) {
        Map<String, String> response = teamService.deleteTeam(teamId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<Map<String, String>> addMemberToTeam(@PathVariable UUID teamId, @PathVariable UUID memberId){
        Map<String,String> response = teamService.addMemberToTeam(teamId, memberId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<Map<String, String>> removeMemberFromTeam(@PathVariable UUID teamId, @PathVariable UUID memberId){
        Map<String,String> response = teamService.removeMemberFromTeam(teamId, memberId);
        return ResponseEntity.ok(response);
    }
}
