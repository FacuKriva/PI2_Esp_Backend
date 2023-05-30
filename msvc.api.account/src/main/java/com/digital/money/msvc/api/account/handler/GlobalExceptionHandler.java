package com.digital.money.msvc.api.account.handler;

import com.digital.money.msvc.api.account.handler.responseError.AlreadyRegisteredResponse;
import com.digital.money.msvc.api.account.handler.responseError.BadRequestResponse;
import com.digital.money.msvc.api.account.handler.responseError.NotFoundResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

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
}