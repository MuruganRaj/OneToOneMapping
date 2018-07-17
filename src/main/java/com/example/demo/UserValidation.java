package com.example.demo;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class UserValidation {


    @NotNull(message = "name should not be empty")
    @NotBlank
    String name;
    @NotNull(message = "address should not be empty")
    String address;
    @Size(min = 1 ,max = 3)
    @NotNull(message = "mobile no should should not empty")
    Integer mobileno;
    @NotNull
    @Min(0)
    String email;

    public UserValidation(@NotNull(message = "name should not be empty") String name, @NotNull(message = "address should not be empty") String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getMobileno() {
        return mobileno;
    }

    public void setMobileno(Integer mobileno) {
        this.mobileno = mobileno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
