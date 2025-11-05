package com.collabspace.collabspace.exceptions;

public class MemberAlreadyAddedToProjectException extends RuntimeException {
    public MemberAlreadyAddedToProjectException(String message) {
        super(message);
    }
}
