package com.collabera.library_app.exception;

import com.collabera.model.CommonError;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.collabera.library_app.exception.constants.ExceptionConstants.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CommonError> handleValidation(ConstraintViolationException ex) {
        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(BAD_REQUEST_EXCEPTION_MESSAGE)
                .details(Collections.singletonList(ex.getMessage()))
                .build();
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<CommonError> handleDatabaseError(DataAccessException ex) {
        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(DATABASE_ERROR_EXCEPTION_MESSAGE)
                .details(List.of(ex.getMostSpecificCause().getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(VALIDATION_ERROR_MESSAGE)
                .details(details)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonError> handleJsonParseError(HttpMessageNotReadableException ex) {
        String message = INVALID_REQUEST_BODY_ERROR_EXCEPTION_MESSAGE;
        if (ex.getCause() instanceof UnrecognizedPropertyException unrecognizedEx) {
            message = "Unrecognized field: " + unrecognizedEx.getPropertyName();
        }

        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .message(message)
                .details(List.of(ex.getMostSpecificCause().getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonError> handleResourceNotFound(ResourceNotFoundException ex) {
        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE)
                .details(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<CommonError> handleConflictException(ConflictException ex) {
        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.CONFLICT.value())
                .message(CONFLICT_EXCEPTION_MESSAGE)
                .details(List.of(ex.getMessage()))
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<CommonError> handleNoHandlerFound(NoHandlerFoundException ex) {
        CommonError error = CommonError.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .message(NOT_FOUND_EXCEPTION_MESSAGE)
                .details(List.of(ex.getMessage()))
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
}
