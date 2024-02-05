package com.appsdeveloperblog.estore.UsersService.rest;


import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginRestModel {
    @NotBlank(message = "userName cannot be blank")
    private String email;
    @NotBlank(message = "password cannot be blank")
    private String password;
}
