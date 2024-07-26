package com.example.rental.controller;

import com.example.rental.entities.UserEntity;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public UserEntity login(@RequestParam String email, @RequestParam String password) {
        return userService.login(email, password);
    }

    @GetMapping("/me")
    public UserEntity getCurrentUser(@RequestParam String email) {
        return userService.getCurrentUser(email);
    }
}
