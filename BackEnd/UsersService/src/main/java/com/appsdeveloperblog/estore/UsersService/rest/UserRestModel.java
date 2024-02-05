package com.appsdeveloperblog.estore.UsersService.rest;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Data
public class UserRestModel {

    @NotBlank(message = "userName cannot be empty")
    private String userName;
    @NotBlank(message = "email cannot be empty")
    private String email;
    @Min(value = 1, message = "Invalid value")
    private int age;
    private String address;
    @NotBlank(message = "phoneNumber cannot be empty")
    private String phoneNumber;
    @NotBlank(message = "Password cannot be empty")
    private String password;
}
