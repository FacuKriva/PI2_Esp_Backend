package com.digital.money.msvc.api.account.handler;

import com.digital.money.msvc.api.account.handler.responseError.AlreadyRegisteredResponse;
import com.digital.money.msvc.api.account.handler.responseError.BadRequestResponse;
import com.digital.money.msvc.api.account.handler.responseError.ForbiddenResponse;
import com.digital.money.msvc.api.account.handler.responseError.NotFoundResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ResourceNotFoundException.class})
    public ResponseEntity<Object> processErrorNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new NotFoundResponse(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<Object> processBadRequestException (BadRequestException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestResponse(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Object> processErrorBadRequest(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestResponse(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> processErrorBadRequest(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BadRequestResponse(ex.getMessage().split(";")[0], request.getRequestURI()));
    }

    @ExceptionHandler({AlreadyRegisteredException.class})
    public ResponseEntity<Object> processErrorAlreadyRegistered(AlreadyRegisteredException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new AlreadyRegisteredResponse(ex.getMessage(), request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .findFirst() // Get the first error message
                .map(ObjectError::getDefaultMessage)
                .orElse("Validation error");

        BadRequestResponse response = new BadRequestResponse(message, request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<Object> processErrorForbidden(ForbiddenException ex, HttpServletRequest request) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ForbiddenResponse(ex.getMessage(), request.getRequestURI()));
    }
}