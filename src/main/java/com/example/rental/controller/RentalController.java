package com.example.rental.controller;

import com.example.rental.dto.RentalRequest;
import com.example.rental.entities.Rental;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.RentalService;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final UserService userService;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";


    @PostMapping(consumes = "multipart/form-data")
    public Rental createRental(@RequestParam("name") String name,
                               @RequestParam("surface") double surface,
                               @RequestParam("price") double price,
                               @RequestParam("description") String description,
                               @RequestPart("picture") MultipartFile file,
                               @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity owner = userService.getCurrentUser(userDetails.getUsername());

        // Vous pouvez traiter le fichier ici, par exemple, en enregistrant l'image et en obtenant son URL
        // Enregistrez l'image et obtenez son URL
        String pictureUrl = saveFile(file);

        // Créez une nouvelle location
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(surface);
        rental.setPrice(price);
        rental.setDescription(description);
        rental.setPicture(pictureUrl);
        rental.setOwner(owner);

        return rentalService.createRental(rental, owner);
    }

    @GetMapping
    public RentalRequest getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        return new RentalRequest(rentals);
    }


    @GetMapping("/{id}")
    public Rental getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public Rental updateRental(@PathVariable Long id,
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

        return rentalService.updateRental(id, rental, owner);
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
            Files.copy(file.getInputStream(), filePath);

            // Retournez l'URL du fichier
            return "http://localhost:8080/uploads/" + uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }
    }
}
