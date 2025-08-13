package com.pomoStudy.exception;

public class ResourceExceptionFactory {

    public static ResourceException notFound(String resource, Object id) {
        String code = (resource + " NOT_FOUND").toUpperCase().replace(' ', '_');
        String message = resource +  " with id " + id + " not found.";
        return new ResourceException(resource, "not_found", code , message);
    }

    public static ResourceException invalidData(String resource, String reason) {
        String code = ("INVALID_" + resource.toUpperCase() + " DATA").replace(' ', '_');
        String message = resource + " data is invalid: " + reason;
        return new ResourceException(resource, "invalid_data", code, message);
    }
}
