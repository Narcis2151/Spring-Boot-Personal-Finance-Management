package org.fna.finance.exception.advice;

import org.fna.finance.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AccountNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handle(AccountNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    public ResponseEntity<ExceptionResponse> handle(CategoryNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    public ResponseEntity<ExceptionResponse> handle(BudgetNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({TransactionNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handle(TransactionNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({InvalidCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handle(InvalidCredentialsException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({DuplicateEmailException.class})
    public ResponseEntity<ExceptionResponse> handle(DuplicateEmailException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({NotEnoughFundsException.class})
    public ResponseEntity<ExceptionResponse> handle(NotEnoughFundsException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest()
                .body(
                        new ExceptionResponse(
                                "Invalid value : " + e.getFieldError().getRejectedValue() +
                                        " for field " + e.getFieldError().getField() +
                                        " with message " + e.getFieldError().getDefaultMessage()
                        )
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> handle(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handle(Exception e) {
        System.out.println(e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ExceptionResponse("An error occurred. Please try again later."));
    }
}
