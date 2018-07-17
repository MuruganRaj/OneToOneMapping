package com.example.demo;

import java.util.Date;
import java.util.List;

public class ApiResponse {

    Date timestamp;
    String message;
    String status;

   List<Wife> sampleList;

    public List<Wife> getSampleList() {
        return sampleList;
    }

    public ApiResponse(Date date, String message, String status) {
        this.timestamp = date;
        this.message = message;
        this.status = status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public ApiResponse(Date date, String message, String status, List<Wife> sampleList) {
        this.timestamp = date;
        this.message = message;
        this.status = status;
        this.sampleList = sampleList;
    }

    public void setDate(Date date) {
        this.timestamp = date;
    }

    public ApiResponse(String message, String status, List<Wife> sampleList) {
        this.message = message;
        this.status = status;
        this.sampleList = sampleList;
    }

    public void setSampleList(List<Wife> sampleList) {
        this.sampleList = sampleList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ApiResponse(String message, String status) {
        this.message = message;
        this.status = status;
    }
}
