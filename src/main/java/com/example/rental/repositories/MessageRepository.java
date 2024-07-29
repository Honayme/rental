package com.example.rental.repositories;

import com.example.rental.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findAllByRentalId(Long rentalId);
}
