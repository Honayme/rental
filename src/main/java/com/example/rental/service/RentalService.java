package com.example.rental.service;

import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;

import java.util.List;

public interface RentalService {
    Rental createRental(Rental rental, UserEntity owner);
    List<Rental> getAllRentals();
    Rental getRentalById(Long id);
    Rental updateRental(Long id, Rental updatedRental, UserEntity owner);
}
