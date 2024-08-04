package com.example.rental.config;

import com.example.rental.service.CustomUserDetailsService;
import com.example.rental.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String[] WHITE_LIST_SWAGGER_URL = {
            "/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui/index.html/**"
    };

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Crée une instance de JwtAuthenticationFilter en passant l'AuthenticationManager et le JwtService.
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http), jwtService);
        // Définit l'URL à traiter par le filtre d'authentification JWT.
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        // Configure HttpSecurity pour désactiver la protection CSRF (car on utilise des tokens JWT pour la sécurité).
        http.csrf(csrf -> csrf.disable())
                // Active la configuration CORS avec la source définie.
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Configure les règles d'autorisation des requêtes HTTP.
                .authorizeHttpRequests(auth -> auth
                        // Permet l'accès à l'endpoint /api/auth/register sans authentification.
                        .requestMatchers("/api/auth/register").permitAll()
                        // Permet l'accès à tous les endpoints sous /api/auth/ sans authentification.
                        .requestMatchers("/api/auth/**").permitAll()
                        // Permet l'accès aux URLs définies dans WHITE_LIST_SWAGGER_URL sans authentification.
                        .requestMatchers(WHITE_LIST_SWAGGER_URL).permitAll()
                        // Permet l'accès aux ressources statiques dans le répertoire uploads sans authentification.
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/api/messages/**").authenticated() // Nécessite une authentification pour les endpoints de l'API des messages
                        .anyRequest().authenticated()
                )
                // Configure la gestion de session pour utiliser une politique sans état (stateless) car on utilise des tokens JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Ajoute le filtre d'authentification JWT à la chaîne de filtres de sécurité.
        http.addFilter(jwtAuthenticationFilter);
        // Ajoute le filtre d'autorisation JWT avant le filtre d'authentification JWT dans la chaîne de filtres.
        http.addFilterBefore(new JwtAuthorizationFilter(jwtService, customUserDetailsService), JwtAuthenticationFilter.class);

        // Construit et retourne la chaîne de filtres de sécurité configurée.
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // Autoriser les requêtes venant de localhost:4200
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
