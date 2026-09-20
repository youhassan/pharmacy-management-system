package com.pharmacy.controller;

import com.pharmacy.model.User;
import com.pharmacy.repository.UserRepository;
import com.pharmacy.service.UserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private UserService userService;
    private UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository)
    {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public User login(@RequestParam String username, @RequestParam String password)
    {
        return userService.login(username, password);
    }

    @PostMapping
    public void addUser(@RequestParam(required = false) String currentUsername, @RequestBody User user)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        userService.addUser(currentUser, user);
    }

    @GetMapping
    public List<User> getAllUsers(@RequestParam(required = false) String currentUsername)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        return userService.getAllUsers(currentUser);
    }

    @PutMapping
    public void updateUser(@RequestParam(required = false) String currentUsername, @RequestParam String oldUsername, @RequestBody User user)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        userService.updateUser(currentUser, oldUsername, user);
    }

    @DeleteMapping
    public void deleteUser(@RequestParam(required = false) String currentUsername, @RequestBody User user)
    {
        User currentUser = currentUsername != null ? userRepository.findById(currentUsername).orElse(null) : null;
        userService.deleteUser(currentUser, user);
    }
}