package com.example.rental.dto;

import lombok.Data;


import java.time.LocalDate;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDate  createdAt;
    private LocalDate  updatedAt;
}
