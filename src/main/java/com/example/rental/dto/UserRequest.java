package com.example.rental.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserRequest {
    private Long id;
    private String name;
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
}