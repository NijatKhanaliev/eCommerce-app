package com.company.demoshop.service.user;

import com.company.demoshop.dto.UserDto;
import com.company.demoshop.model.User;
import com.company.demoshop.request.CreateUserRequest;
import com.company.demoshop.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertToDto(User user);

    User getAuthenticatedUser();
}
