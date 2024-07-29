package com.example.rental.controller;

import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;

    @PostMapping
    public Rental createRental(@RequestBody Rental rental, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());
        return rentalService.createRental(rental, owner);
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public Rental getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @PutMapping("/{id}")
    public Rental updateRental(@PathVariable Long id, @RequestBody Rental updatedRental, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());
        return rentalService.updateRental(id, updatedRental, owner);
    }
}
