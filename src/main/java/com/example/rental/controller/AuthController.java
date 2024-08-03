package com.example.rental.controller;

import com.example.rental.dto.UserRequest;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.JwtService;
import com.example.rental.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        // Valide la requête en vérifiant que le nom, l'email et le mot de passe ne sont pas null.
        if (userRequest.getName() == null || userRequest.getEmail() == null || userRequest.getPassword() == null) {
            // Retourne une réponse 400 Bad Request avec un message d'erreur si une validation échoue.
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid input\"}");
        }

        // Crée un nouvel utilisateur en utilisant les informations fournies dans la requête.
        UserEntity newUser = new UserEntity();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        Date now = new Date();
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        // Enregistre le nouvel utilisateur en appelant le service userService.register.
        UserEntity registeredUser = userService.register(newUser);

        // Authentifie l'utilisateur nouvellement enregistré pour générer les UserDetails nécessaires.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(newUser.getEmail(), userRequest.getPassword())
        );
        // Récupère les détails de l'utilisateur authentifié.
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // Génère un token JWT pour l'utilisateur authentifié.
        String token = jwtService.generateToken(userDetails);

        // Retourne une réponse 200 OK avec le token JWT au format JSON.
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }



    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);
    }

    @GetMapping("/me")
    public UserEntity getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }
        return userService.getCurrentUser(userDetails.getUsername());
    }
}
