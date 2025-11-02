package com.collabspace.collabspace.utils;

import com.collabspace.collabspace.dto.TeamResponseDto;
import com.collabspace.collabspace.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMapper {
   public static TeamResponseDto toTeamResponse(Team team) {
       TeamResponseDto teamResponseDto = new TeamResponseDto();
       teamResponseDto.setTeamId(team.getId());
       teamResponseDto.setName(team.getName());
       teamResponseDto.setCreatedBy(team.getCreatedBy());
       teamResponseDto.setMemberIds(team.getMemberIds());
       return teamResponseDto;
   }
}
