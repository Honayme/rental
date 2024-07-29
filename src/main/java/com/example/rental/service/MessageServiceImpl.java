package com.example.rental.service;

import com.example.rental.entities.Message;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message createMessage(Message message, Rental rental, UserEntity user) {
        message.setRental(rental);
        message.setUser(user);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> getMessagesByRentalId(Long rentalId) {
        return messageRepository.findAllByRentalId(rentalId);
    }
}