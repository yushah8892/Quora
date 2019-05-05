package com.upgrad.quora.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(SignUpRestrictedException.class)
    public ResponseEntity<ErrorResponse> userAlreadyPresentException(SignUpRestrictedException sre, WebRequest wr){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(sre.getCode()).message(sre.getErrorMessage()), HttpStatus.NOT_ACCEPTABLE);
    }


    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(AuthenticationFailedException sre, WebRequest wr){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(sre.getCode()).message(sre.getErrorMessage()), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(SignOutRestrictedException.class)
    public ResponseEntity<ErrorResponse> authenticationFailedException(SignOutRestrictedException sre, WebRequest wr){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(sre.getCode()).message(sre.getErrorMessage()), HttpStatus.NOT_ACCEPTABLE);
    }
}
