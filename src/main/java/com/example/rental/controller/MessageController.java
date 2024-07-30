package com.example.rental.controller;

import com.example.rental.dto.MessageRequest;
import com.example.rental.entities.Message;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.MessageService;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    private final RentalService rentalService;
    private final UserService userService;

    @PostMapping
    public Message createMessage(@RequestBody MessageRequest messageRequest) {
        logger.debug("Received message request: {}", messageRequest);

        Long userId = messageRequest.getUserId();
        Long rentalId = messageRequest.getRentalId();
        String messageContent = messageRequest.getMessage();

        logger.debug("User ID: {}", userId);
        logger.debug("Rental ID: {}", rentalId);
        logger.debug("Message: {}", messageContent);

        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (rentalId == null) {
            throw new IllegalArgumentException("Rental ID must not be null");
        }
        if (messageContent == null) {
            throw new IllegalArgumentException("Message content must not be null");
        }

        UserEntity user = userService.getCurrentUserById(userId);
        Rental rental = rentalService.getRentalById(rentalId);

        Message message = new Message();
        message.setMessage(messageContent);
        message.setRental(rental);
        message.setUser(user);
        return messageService.createMessage(message, rental, user);
    }


    @GetMapping("/rental/{rentalId}")
    public List<Message> getMessagesByRentalId(@PathVariable Long rentalId) {
        return messageService.getMessagesByRentalId(rentalId);
    }
}
