package com.example.rental.service;

import com.example.rental.entities.UserEntity;

public interface UserService {
    UserEntity register(UserEntity user);
    UserEntity login(String email, String password);
    UserEntity getCurrentUser(String email);
}
