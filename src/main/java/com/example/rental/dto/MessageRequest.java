package com.example.rental.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MessageRequest {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("rental_id")
    private Long rentalId;
}
