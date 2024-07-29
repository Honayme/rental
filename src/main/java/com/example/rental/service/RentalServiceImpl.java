package com.example.rental.service;

import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.repositories.RentalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Override
    public Rental createRental(Rental rental, UserEntity owner) {
        rental.setOwner(owner);
        return rentalRepository.save(rental);
    }

    @Override
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Override
    public Rental getRentalById(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    @Override
    public Rental updateRental(Long id, Rental updatedRental, UserEntity owner) {
        Rental existingRental = getRentalById(id);
        if (existingRental != null && existingRental.getOwner().equals(owner)) {
            existingRental.setName(updatedRental.getName());
            existingRental.setSurface(updatedRental.getSurface());
            existingRental.setPrice(updatedRental.getPrice());
            existingRental.setPicture(updatedRental.getPicture());
            existingRental.setDescription(updatedRental.getDescription());
            return rentalRepository.save(existingRental);
        }
        return null;
    }
}
