package com.collabspace.collabspace.handlers;

import com.collabspace.collabspace.dto.ErrorDto;
import com.collabspace.collabspace.exceptions.MemberAlreadyAddedToProjectException;
import com.collabspace.collabspace.exceptions.MemberIsNotInTheTeamException;
import com.collabspace.collabspace.exceptions.ProjectDoesNotExistException;
import com.collabspace.collabspace.exceptions.TeamDoesNotExistException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
    @ExceptionHandler(ProjectDoesNotExistException.class)
    public ResponseEntity<ErrorDto> handleProjectDoesNotExistException(ProjectDoesNotExistException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
    @ExceptionHandler(TeamDoesNotExistException.class)
    public ResponseEntity<ErrorDto> handleTeamDoesNotExistException(TeamDoesNotExistException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
    @ExceptionHandler(MemberAlreadyAddedToProjectException.class)
    public ResponseEntity<ErrorDto> handleMemberAlreadyAddedToProjectException(MemberAlreadyAddedToProjectException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
    @ExceptionHandler(MemberIsNotInTheTeamException.class)
    public ResponseEntity<ErrorDto> handleMemberIsNotInTheTeam(MemberIsNotInTheTeamException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setMessage(ex.getMessage());
        return ResponseEntity.badRequest().body(errorDto);
    }
}
