package com.example.rental.controller;

import com.example.rental.dto.CustomApiResponse;
import com.example.rental.dto.MessageRequest;
import com.example.rental.entities.Message;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.MessageService;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageService messageService;
    private final RentalService rentalService;
    private final UserService userService;

    @Operation(summary = "Create a new message")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CustomApiResponse> createMessage(@RequestBody MessageRequest messageRequest) {
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
        messageService.createMessage(message, rental, user);

        CustomApiResponse customApiResponse = new CustomApiResponse("Message sent with success");
        return new ResponseEntity<>(customApiResponse, HttpStatus.CREATED);
    }

    @Operation(summary = "Get messages by rental ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Messages retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Message.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    })
    @GetMapping("/rental/{rentalId}")
    public List<Message> getMessagesByRentalId(@PathVariable Long rentalId) {
        return messageService.getMessagesByRentalId(rentalId);
    }
}
