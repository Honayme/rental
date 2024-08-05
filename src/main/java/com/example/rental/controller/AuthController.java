package com.example.rental.controller;

import com.example.rental.dto.UserRequest;
import com.example.rental.dto.UserResponse;
import com.example.rental.entities.UserEntity;
import com.example.rental.service.JwtService;
import com.example.rental.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Collections;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"token\": \"jwt\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content)
    })
    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody UserRequest userRequest) {
        if (userRequest.getName() == null || userRequest.getEmail() == null || userRequest.getPassword() == null) {
            return ResponseEntity.badRequest().body("{\"error\": \"Invalid input\"}");
        }

        UserEntity newUser = new UserEntity();
        newUser.setName(userRequest.getName());
        newUser.setEmail(userRequest.getEmail());
        newUser.setPassword(userRequest.getPassword());
        Date now = new Date();
        newUser.setCreatedAt(now);
        newUser.setUpdatedAt(now);

        UserEntity registeredUser = userService.register(newUser);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(newUser.getEmail(), userRequest.getPassword())
        );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"token\": \"jwt\"}"))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/auth/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(userDetails);
    }

    @Operation(summary = "Get current authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @GetMapping("/auth/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            throw new RuntimeException("Unauthorized");
        }
        UserEntity userEntity = userService.getCurrentUser(userDetails.getUsername());
        return convertToDTO(userEntity);
    }

    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserEntity.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        UserEntity user = userService.getCurrentUserById(id);
        if (user == null) {
            return ResponseEntity.status(404).body(null);
        }
        return ResponseEntity.ok(convertToDTO(user));
    }

    private UserResponse convertToDTO(UserEntity userEntity) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(userEntity.getId());
        userResponse.setName(userEntity.getName());
        userResponse.setEmail(userEntity.getEmail());
        userResponse.setCreatedAt(convertToLocalDate(userEntity.getCreatedAt()));
        userResponse.setUpdatedAt(convertToLocalDate(userEntity.getUpdatedAt()));
        return userResponse;
    }

    private LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}
