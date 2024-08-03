package com.example.rental.controller;

import com.example.rental.dto.ApiResponse;
import com.example.rental.dto.RentalRequest;
import com.example.rental.dto.RentalListResponse;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
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

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse> createRental(@RequestParam("name") String name,
                                                    @RequestParam("surface") double surface,
                                                    @RequestParam("price") double price,
                                                    @RequestParam("description") String description,
                                                    @RequestPart("picture") MultipartFile file,
                                                    @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());

        // Traitez le fichier et obtenez son URL
        String pictureUrl = saveFile(file);

        // Créez une nouvelle location
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setPicture(pictureUrl);
        rental.setOwner(owner);

        rentalService.createRental(rental, owner);

        // Retourner un message de succès
        ApiResponse response = new ApiResponse("Rental created!");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public RentalListResponse getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        List<RentalRequest> rentalRequests = rentals.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new RentalListResponse(rentalRequests);
    }

    @GetMapping("/{id}")
    public RentalRequest getRentalById(@PathVariable Long id) {
        Rental rental = rentalService.getRentalById(id);
        return convertToDTO(rental);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Rental> updateRental(@PathVariable Long id,
                                               @RequestParam("name") String name,
                                               @RequestParam("surface") double surface,
                                               @RequestParam("price") double price,
                                               @RequestParam("description") String description,
                                               @RequestPart(value = "picture", required = false) MultipartFile file,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());

        // Récupérez la location existante
        Rental rental = rentalService.getRentalById(id);
        if (rental == null || !rental.getOwner().equals(owner)) {
            throw new RuntimeException("Rental not found or you are not the owner");
        }

        // Mettez à jour les champs de la location
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);

        // Mettez à jour l'image si un nouveau fichier est fourni
        if (file != null && !file.isEmpty()) {
            String pictureUrl = saveFile(file);
            rental.setPicture(pictureUrl);
        }

        Rental updatedRental = rentalService.updateRental(id, rental, owner);
        return ResponseEntity.ok(updatedRental);
    }

    // Méthode pour enregistrer le fichier
    private String saveFile(MultipartFile file) {
        try {
            // Créez le répertoire de téléchargement s'il n'existe pas
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Créez un nom de fichier unique pour éviter les conflits
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;

            // Enregistrez le fichier sur le serveur
            Path filePath = Paths.get(UPLOAD_DIR, uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retournez l'URL du fichier
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
