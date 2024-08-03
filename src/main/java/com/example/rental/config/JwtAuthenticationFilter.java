package com.example.rental.config;

import com.example.rental.service.JwtService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    // Déclare un logger pour enregistrer les événements, les erreurs, etc.
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    // Déclare des variables pour le gestionnaire d'authentification et le service JWT
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    // Constructeur pour initialiser le filtre avec le gestionnaire d'authentification et le service JWT
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        // Définit l'URL de traitement des requêtes de ce filtre
        setFilterProcessesUrl("/api/auth/login");
    }

    // Surcharge de la méthode attemptAuthentication pour tenter une authentification
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // Création d'un ObjectMapper pour lire et écrire des objets JSON
            ObjectMapper objectMapper = new ObjectMapper();
            // Lit les identifiants de la requête en tant que Map
            Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), new TypeReference<Map<String, String>>() {});
            // Récupère l'email et le mot de passe de la Map
            String email = credentials.get("email");
            String password = credentials.get("password");

            // Enregistre un message de débogage avec l'email et le mot de passe
            logger.debug("Attempting authentication with email: {} and password: {}", email, password);

            // Vérifie si l'email ou le mot de passe est null et lève une exception si c'est le cas
            if (email == null || password == null) {
                throw new RuntimeException("Email or Password not provided");
            }

            // Crée un jeton d'authentification avec l'email et le mot de passe
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);

            // Tente d'authentifier le jeton en utilisant le gestionnaire d'authentification
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            // Lève une RuntimeException en cas d'erreur d'entrée/sortie
            throw new RuntimeException(e);
        }
    }

    // Surcharge de la méthode successfulAuthentication pour traiter une authentification réussie
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // Récupère les détails de l'utilisateur authentifié
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        // Génère un token JWT pour l'utilisateur
        String token = jwtService.generateToken(userDetails);

        // Ajoute le token à l'en-tête de la réponse
        response.addHeader("Authorization", "Bearer " + token);
        // Prépare le corps de la réponse
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);

        // Écrit le token dans le corps de la réponse
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }
}

