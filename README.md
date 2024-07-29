# Rental Application

This is a Spring Boot application for managing rental services.

## Prerequisites

- Java 17
- Maven
- MySQL

## Dependencies

- Spring Boot Starter Data JPA
- Spring Boot Starter Security
- Spring Boot Starter Web
- Spring Boot DevTools
- MySQL Connector/J
- Lombok
- Spring Boot Starter Test
- Spring Security Test
- Auth0 Java JWT
- Springdoc OpenAPI Starter WebMVC UI

## Getting Started

### Step 1: Clone the Repository

```bash
git clone <repository-url>
cd rental
```

### Step 2: Set Up the Database

Create a MySQL database for the application.

```sql
CREATE DATABASE rentaldb;
```

### Step 3: Configure the Application

Update the `application.properties` file with your database configuration.

```properties
# src/main/resources/application.properties

spring.datasource.url=jdbc:mysql://localhost:3306/rentaldb
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
```

### Step 4: Build and Run the Application

Use Maven to build and run the application.

```bash
mvn clean install
mvn spring-boot:run
```

### Step 5: Access Swagger UI

Once the application is running, you can access the Swagger UI to explore the API documentation at:

```
http://localhost:8080/swagger-ui.html
```

## Security Configuration

The application uses JWT for authentication. The following URLs are publicly accessible:

- `/api/auth/**`
- `/swagger-ui/**`
- `/v3/api-docs/**`

Other URLs require authentication.

### Example Security Configuration

```java
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(http), jwtService);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilter(jwtAuthenticationFilter);
        http.addFilterBefore(new JwtAuthorizationFilter(jwtService, customUserDetailsService), JwtAuthenticationFilter.class);

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
}
```

## Using Postman for Testing

Here are some example requests you can use in Postman:

### Register a New User

- **URL**: `POST http://localhost:8080/api/auth/register`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "newuser@example.com",
    "password": "password",
    "name": "New User"
  }
  ```

### Login

- **URL**: `POST http://localhost:8080/api/auth/login`
- **Headers**: `Content-Type: application/json`
- **Body**:
  ```json
  {
    "email": "newuser@example.com",
    "password": "password"
  }
  ```

### Get Current User

- **URL**: `GET http://localhost:8080/api/auth/me`
- **Headers**: `Authorization: Bearer <your_jwt_token>`

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Thanks to the Spring Boot team for their amazing framework.
- Thanks to the Auth0 team for their JWT library.
- Thanks to the Springdoc team for their OpenAPI library.
