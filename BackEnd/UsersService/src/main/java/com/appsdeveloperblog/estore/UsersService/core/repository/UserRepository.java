package com.appsdeveloperblog.estore.UsersService.core.repository;

import com.appsdeveloperblog.estore.UsersService.core.data.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    UserEntity findByUserName(String userName);
    UserEntity findByEmail(String email);
}
