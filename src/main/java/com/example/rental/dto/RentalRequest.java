package com.example.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalRequest {
    private Long id;
    private String name;
    private Double surface;
    private Double price;
    private String picture;
    private String description;
    @JsonProperty("owner_id")
    private Long owner_id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}