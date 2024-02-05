package com.appsdeveloperblog.estore.UsersService.service;

import com.appsdeveloperblog.estore.UsersService.core.data.UserEntity;
import com.appsdeveloperblog.estore.UsersService.core.repository.UserRepository;
import com.appsdeveloperblog.estore.UsersService.rest.UserController;
import com.appsdeveloperblog.estore.UsersService.rest.UserLoginRestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    private Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(UserEntity user){
        try {
            userRepository.save(user);
            return "Registered user successfully";
        }
        catch (Exception ex){
            return "Error in registering user: " + ex.getMessage();
        }
    }

    public boolean authenticateUser(UserLoginRestModel userLoginRestModel){
        Boolean result = false;
        try
        {
            LOGGER.info(userLoginRestModel.getPassword());
            LOGGER.info(userRepository.findByEmail(userLoginRestModel.getEmail()).toString());
            String usersPassword = userRepository.findByEmail(userLoginRestModel.getEmail()).getPassword();
            if (userLoginRestModel.getPassword().equals(usersPassword)) {
                LOGGER.info("password matched");
                result = true;
            }
            else {
                LOGGER.info("password didn't match");
            }
        }
        catch (Exception ex){
            LOGGER.info(ex.getMessage());
        }
        return result;
    }

    public UserEntity getUser(String email){
        return userRepository.findByEmail(email);
    }

    public List<UserEntity> getAllUsers()
    {
        return userRepository.findAll();
    }
}
