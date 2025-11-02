package com.collabspace.collabspace.exceptions;

public class TeamDoesNotExistException extends RuntimeException {
    public TeamDoesNotExistException(String message) {
        super(message);
    }
}
