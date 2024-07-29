package com.example.rental.service;

import com.example.rental.entities.Message;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;

import java.util.List;

public interface MessageService {
    Message createMessage(Message message, Rental rental, UserEntity user);
    List<Message> getMessagesByRentalId(Long rentalId);
}
