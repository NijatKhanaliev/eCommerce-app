package com.company.demoshop.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String email;
}
