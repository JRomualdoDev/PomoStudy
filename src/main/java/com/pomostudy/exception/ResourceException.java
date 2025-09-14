package com.pomostudy.exception;

public class ResourceException extends RuntimeException{

    private final String resource;
    private final String action;
    private final String code;

    public ResourceException(String resource, String action, String code, String message) {
        super(message);
        this.resource = resource;
        this.action = action;
        this.code = code;
    }

    public String getResource() {
        return resource;
    }

    public String getAction() {
        return action;
    }

    public String getCode() {
        return code;
    }
}
