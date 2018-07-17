package com.example.demo;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {

    private HttpStatus status;
    private LocalDateTime timestamp;
    private String message;
    private String debugmessage;
    private List<ApisubError> subErrors;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDebugmessage() {
        return debugmessage;
    }

    public void setDebugmessage(String debugmessage) {
        this.debugmessage = debugmessage;
    }

    public List<ApisubError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApisubError> subErrors) {
        this.subErrors = subErrors;
    }

    public ApiError(HttpStatus status,Throwable ex) {
        this.status = status;
        this.debugmessage=ex.getLocalizedMessage();
    }

    public ApiError(String message) {
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, Throwable throwable) {
        this.status = status;
        this.message = message;
        this.debugmessage=throwable.getLocalizedMessage();
    }
}
