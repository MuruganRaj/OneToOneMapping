package com.example.demo;

import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.sql.SQLException;

@ControllerAdvice
public class RestExceptionHandler  {

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> EntityNotFoundException(EntityNotFoundException e){

        ApiError apiError = new ApiError("NOT_FOUND");

        apiError.setMessage(e.getMessage());
        return new ResponseEntity<Object>(apiError,HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(SQLException.class)
    protected ResponseEntity<Object> sqlexception(SQLException e){

        ApiError apiError = new ApiError("NOT_FOUND");

        apiError.setMessage(e.getMessage());
        return new ResponseEntity<Object>(apiError,HttpStatus.NOT_FOUND);

    }
}
