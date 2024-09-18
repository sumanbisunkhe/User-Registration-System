package com.example.userregistrationsystem.controller;

import com.example.userregistrationsystem.dto.UserDto;
import com.example.userregistrationsystem.exceptions.UserNotFoundException;
import com.example.userregistrationsystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String home(Model model) {
        return "users/home";
    }

    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users/list";
    }

    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "users/create";
    }

    @PostMapping("/create")
    public String saveUser(@Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/create";
        }
        userService.saveUser(userDto);
        return "redirect:/users/list";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable("id") Long id, Model model) {
        try {
            UserDto userDto = userService.getUserById(id);
            model.addAttribute("user", userDto);
            return "users/view";
        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "users/error";
        }
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable("id") Long id, Model model) {
        try {
            UserDto userDto = userService.getUserById(id);
            model.addAttribute("userDto", userDto);
            return "users/edit";
        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "users/error";
        }
    }

    @PostMapping("/{id}/edit")
    public String updateUser(@PathVariable("id") Long id, @Valid @ModelAttribute("userDto") UserDto userDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "users/edit";
        }
        try {
            userDto.setId(id);
            userService.updateUser(id, userDto);
        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "users/error";
        }
        return "redirect:/users/list";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id, Model model) {
        try {
            userService.deleteUserById(id);
        } catch (UserNotFoundException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
            return "users/error";
        }
        return "redirect:/users/list";
    }
}
