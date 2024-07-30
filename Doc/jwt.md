### Schéma d'Architecture

```plaintext
+------------------+                +---------------------+
|                  |                |                     |
|  AuthController  +---------------->  UserService        |
|                  |                |                     |
+--------+---------+                +---+-----------------+
         |                              |
         |                              |
         v                              v
+--------+---------+                +---+-----------------+
|                  |                |                     |
| Authentication   +<---------------+  UserRepository    |
|   Manager        |                |                     |
+--------+---------+                +---------------------+
         |
         |
         v
+--------+---------+
|                  |
| JwtService       |
|                  |
+--------+---------+
         |
         v
+--------+---------+
|                  |
| CustomUserDetails|
|     Service      |
|                  |
+--------+---------+
         |
         v
+--------+---------+
|                  |
| JwtAuthentication|
|     Filter       |
|                  |
+--------+---------+
         |
         v
+--------+---------+
|                  |
| JwtAuthorization |
|     Filter       |
|                  |
+------------------+
```

### Explication des Interactions

1. **AuthController** :
    - **Rôle** : Point d'entrée pour les requêtes d'authentification et d'inscription.
    - **Fonctionnement** :
        - Lorsqu'un utilisateur tente de s'inscrire, la requête est envoyée à `UserService` pour enregistrer l'utilisateur.
        - Lorsqu'un utilisateur tente de se connecter, la requête est envoyée à `AuthenticationManager` pour authentification.

2. **UserService** :
    - **Rôle** : Gestion des utilisateurs, y compris l'inscription et la récupération des détails de l'utilisateur.
    - **Fonctionnement** :
        - Pour l'inscription, il encode le mot de passe et sauvegarde l'utilisateur dans `UserRepository`.
        - Pour la connexion, il vérifie les informations d'identification de l'utilisateur et renvoie les détails de l'utilisateur.

3. **UserRepository** :
    - **Rôle** : Interface pour accéder à la base de données des utilisateurs.
    - **Fonctionnement** :
        - Fournit des méthodes pour rechercher les utilisateurs par email et enregistrer de nouveaux utilisateurs.

4. **AuthenticationManager** :
    - **Rôle** : Gère le processus d'authentification.
    - **Fonctionnement** :
        - Utilisé par `AuthController` pour authentifier les informations d'identification de l'utilisateur.
        - Si l'authentification est réussie, il génère un token JWT via `JwtService`.

5. **JwtService** :
    - **Rôle** : Génération et validation des tokens JWT.
    - **Fonctionnement** :
        - Génère un token JWT lors d'une authentification réussie.
        - Valide les tokens JWT dans les requêtes entrantes.

6. **CustomUserDetailsService** :
    - **Rôle** : Fournit les détails de l'utilisateur pour l'authentification.
    - **Fonctionnement** :
        - Charge les détails de l'utilisateur à partir de `UserRepository` pour `AuthenticationManager`.

7. **JwtAuthenticationFilter** :
    - **Rôle** : Filtre d'authentification pour les requêtes de connexion.
    - **Fonctionnement** :
        - Intercepte les requêtes de connexion, tente d'authentifier l'utilisateur via `AuthenticationManager`.
        - Si l'authentification réussit, génère un token JWT et l'ajoute à la réponse.

8. **JwtAuthorizationFilter** :
    - **Rôle** : Filtre d'autorisation pour les requêtes protégées.
    - **Fonctionnement** :
        - Intercepte toutes les requêtes entrantes.
        - Vérifie si la requête contient un token JWT valide via `JwtService`.
        - Si le token est valide, charge les détails de l'utilisateur via `CustomUserDetailsService` et définit l'utilisateur authentifié dans le contexte de sécurité.

### Fonctionnement Global

1. **Inscription** :
    - L'utilisateur envoie une requête POST à `/api/auth/register`.
    - `AuthController` appelle `UserService` pour enregistrer l'utilisateur.
    - `UserService` encode le mot de passe et enregistre l'utilisateur dans `UserRepository`.

2. **Connexion** :
    - L'utilisateur envoie une requête POST à `/api/auth/login` avec ses informations d'identification.
    - `JwtAuthenticationFilter` intercepte la requête et tente d'authentifier l'utilisateur via `AuthenticationManager`.
    - `AuthenticationManager` utilise `CustomUserDetailsService` pour charger les détails de l'utilisateur.
    - Si l'authentification réussit, `JwtService` génère un token JWT et l'ajoute à la réponse.

3. **Accès aux Ressources Protégées** :
    - L'utilisateur envoie une requête avec le token JWT dans l'en-tête `Authorization`.
    - `JwtAuthorizationFilter` intercepte la requête et vérifie le token JWT via `JwtService`.
    - Si le token est valide, `CustomUserDetailsService` charge les détails de l'utilisateur et définit l'utilisateur authentifié dans le contexte de sécurité.
    - La requête est autorisée à accéder aux ressources protégées.
