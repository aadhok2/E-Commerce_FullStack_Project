package com.appsdeveloperblog.estore.UsersService.rest;


import com.appsdeveloperblog.estore.UsersService.core.data.UserEntity;
import com.appsdeveloperblog.estore.UsersService.core.repository.UserRepository;
import com.appsdeveloperblog.estore.UsersService.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private UserRepository userRepository;

    private UserService userService;

    private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> registerUser(@RequestBody UserRestModel userRestModel){

        String response = "";
        try
        {
            String userId = UUID.randomUUID().toString();
            UserEntity user = new UserEntity(
                    userId,
                    userRestModel.getEmail(),
                    userRestModel.getUserName(),
                    userRestModel.getAge(),
                    userRestModel.getAddress(),
                    userRestModel.getPhoneNumber(),
                    userRestModel.getPassword()
            );
            LOGGER.info(userService.registerUser(user));
            LOGGER.info("Registered user " + userRestModel.getUserName() + " successfully");
            return new ResponseEntity<>(user,HttpStatus.OK);
        }
        catch (Exception ex)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserEntity> loginUser(@RequestBody UserLoginRestModel userLoginRestModel) {
        try {
            LOGGER.info(userLoginRestModel.getEmail(),userLoginRestModel.getPassword());
            if(userService.authenticateUser(userLoginRestModel)) {
                // Assuming userService returns the UserEntity upon successful authentication
                UserEntity authenticatedUser = userService.getUser(userLoginRestModel.getEmail());
                LOGGER.info("Found user " + userLoginRestModel.getEmail() + " successfully.");
                return new ResponseEntity<>(authenticatedUser, HttpStatus.OK);
            } else
            {
                LOGGER.info("User " + userLoginRestModel.getEmail() + " not registered.");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            LOGGER.error("Error during user login", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getUsers()
    {
        try
        {
            return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
        }
        catch (Exception ex)
        {
            LOGGER.error("Error during user login", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}

