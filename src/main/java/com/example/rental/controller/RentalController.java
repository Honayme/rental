package com.example.rental.controller;

import com.example.rental.dto.CustomApiResponse;
import com.example.rental.dto.RentalRequest;
import com.example.rental.dto.RentalListResponse;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    @Operation(summary = "Create a new rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rental created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<CustomApiResponse> createRental(@RequestParam("name") String name,
                                                          @RequestParam("surface") double surface,
                                                          @RequestParam("price") double price,
                                                          @RequestParam("description") String description,
                                                          @RequestPart("picture") MultipartFile file,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());

        String pictureUrl = saveFile(file);

        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setPicture(pictureUrl);
        rental.setOwner(owner);

        rentalService.createRental(rental, owner);

        CustomApiResponse response = new CustomApiResponse("Rental created!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get all rentals")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rentals retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalListResponse.class)))
    })
    @GetMapping
    public RentalListResponse getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalRequest> rentalRequests = rentals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new RentalListResponse(rentalRequests);
    }

    @Operation(summary = "Get rental by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalRequest.class))),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    })
    @GetMapping("/{id}")
    public RentalRequest getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return convertToDTO(rental);
    }

    @Operation(summary = "Update a rental")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CustomApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rental not found or you are not the owner", content = @Content)
    })
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<CustomApiResponse> updateRental(@PathVariable Long id,
                                                          @RequestParam("name") String name,
                                                          @RequestParam("surface") double surface,
                                                          @RequestParam("price") double price,
                                                          @RequestParam("description") String description,
                                                          @RequestPart(value = "picture", required = false) MultipartFile file,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());

        Rental rental = rentalService.getRentalById(id);
        if (rental == null || !rental.getOwner().equals(owner)) {
            throw new RuntimeException("Rental not found or you are not the owner");
        }

        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        if (file != null && !file.isEmpty()) {
            String pictureUrl = saveFile(file);
            rental.setPicture(pictureUrl);
        }

        rentalService.updateRental(id, rental, owner);
        CustomApiResponse response = new CustomApiResponse("Rental updated!");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private String saveFile(MultipartFile file) {
        try {
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return "http://localhost:3001/uploads/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }

    private RentalRequest convertToDTO(Rental rental) {
        if (rental == null) {
            return null;
        }
        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setId(rental.getId());
        rentalRequest.setName(rental.getName());
        rentalRequest.setSurface(rental.getSurface());
        rentalRequest.setPrice(rental.getPrice());
        rentalRequest.setPicture(rental.getPicture());
        rentalRequest.setDescription(rental.getDescription());
        rentalRequest.setOwner_id(rental.getOwner().getId());
        rentalRequest.setCreatedAt(rental.getCreatedAt());
        rentalRequest.setUpdatedAt(rental.getUpdatedAt());
        return rentalRequest;
    }
}
