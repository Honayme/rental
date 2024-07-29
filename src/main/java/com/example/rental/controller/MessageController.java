package com.example.rental.controller;

import com.example.rental.entities.Message;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.MessageService;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final RentalService rentalService;
    private final UserService userService;

    @PostMapping
    public Message createMessage(@RequestParam Long rentalId, @RequestBody String messageContent, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getCurrentUser(userDetails.getUsername());
        Rental rental = rentalService.getRentalById(rentalId);
        Message message = new Message();
        message.setMessage(messageContent);
        return messageService.createMessage(message, rental, user);
    }

    @GetMapping("/rental/{rentalId}")
    public List<Message> getMessagesByRentalId(@PathVariable Long rentalId) {
        return messageService.getMessagesByRentalId(rentalId);
    }
}
