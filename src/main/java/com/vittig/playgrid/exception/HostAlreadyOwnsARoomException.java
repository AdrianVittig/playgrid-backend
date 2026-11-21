package com.vittig.playgrid.exception;

public class HostAlreadyOwnsARoomException extends RuntimeException {
    public HostAlreadyOwnsARoomException(String message) {
        super(message);
    }
}
