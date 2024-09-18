package com.example.userregistrationsystem.repo.impl;

import com.example.userregistrationsystem.dto.UserDto;
import com.example.userregistrationsystem.exceptions.UserNotFoundException;
import com.example.userregistrationsystem.exceptions.UsernameAlreadyExistsException;
import com.example.userregistrationsystem.model.User;
import com.example.userregistrationsystem.repo.UserRepo;
import com.example.userregistrationsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Autowired
    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDto saveUser(UserDto userDto) {
        Optional<User> existingUser = userRepo.findByUsername(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        // Convert to Entity
        User user = ConvertToEntity(userDto);

        // Save the user entity
        User savedUser = userRepo.save(user);

        return ConvertToDto(savedUser);
    }


    @Override
    public List<UserDto> getAllUsers() {
        return userRepo.findAll().stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());

    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepo.findById(id)
                .map(this::ConvertToDto)
                .orElse(null);
    }

    @Override
    public void deleteUserById(Long id) {
        userRepo.deleteById(id);

    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        // Fetch the current user by ID
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        // Check if the new username is already used by another user
        Optional<User> existingUser = userRepo.findByUsername(userDto.getUsername());
        if (existingUser.isPresent() && !existingUser.get().getId().equals(id)) {
            // If a different user already has this username, throw exception
            throw new UsernameAlreadyExistsException("Username '" + userDto.getUsername() + "' is already taken.");
        }

        // Update user details
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());

        // Save the updated user entity
        User updatedUser = userRepo.save(user);

        // Convert the updated entity back to DTO and return
        return ConvertToDto(updatedUser);
    }



    //Convert To Entity
    private User ConvertToEntity(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setPhone(userDto.getPhone());
        return user;
    }

    //Convert To Dto
    private UserDto ConvertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        return userDto;
    }

}
