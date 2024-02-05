package com.appsdeveloperblog.estore.UsersService.core.data;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@Table(name="users")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    @Id
    @Column(unique = true)
    private String userId;
    @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String userName;
    @Min(value = 1, message = "Invalid value")
    private int age;
    private String address;
    @NotNull
    private String phoneNumber;
    @NotNull
    private String password;
}
