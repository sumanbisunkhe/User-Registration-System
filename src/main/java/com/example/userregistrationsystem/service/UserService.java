package com.example.userregistrationsystem.service;

import com.example.userregistrationsystem.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto saveUser(UserDto userDto);
    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    void deleteUserById(Long id);
    UserDto updateUser(Long id, UserDto userDto);
}
