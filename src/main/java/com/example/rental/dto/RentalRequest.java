package com.example.rental.dto;

import com.example.rental.entities.Rental;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalRequest {
    private List<Rental> rentals;
}