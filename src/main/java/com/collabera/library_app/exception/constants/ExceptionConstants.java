package com.collabera.library_app.exception.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ExceptionConstants {
    public static final String INVALID_REQUEST_BODY_ERROR_EXCEPTION_MESSAGE = "Invalid request body. Please check the fields.";
    public static final String BAD_REQUEST_EXCEPTION_MESSAGE = "Bad request";
    public static final String VALIDATION_ERROR_MESSAGE = "Validation failed for request body";
    public static final String DATABASE_ERROR_EXCEPTION_MESSAGE = "Database error";
    public static final String RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE = "Resource not found";
    public static final String CONFLICT_EXCEPTION_MESSAGE = "Conflict occurred";
    public static final String NOT_FOUND_EXCEPTION_MESSAGE = "Resource not found";
}
