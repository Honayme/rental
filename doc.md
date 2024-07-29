### Commandes Maven Courantes et Leur Utilisation

1. **`mvn clean`** :
    - **Utilisation** : Cette commande supprime tous les fichiers générés par les builds précédents dans le répertoire `target`.
    - **Quand l'utiliser** : Utilisez cette commande avant de lancer un nouveau build pour vous assurer que vous partez d'un état propre. Cela permet d'éviter les problèmes liés à d'anciens fichiers de build.
    - **Exemple** :
      ```sh
      mvn clean
      ```

2. **`mvn compile`** :
    - **Utilisation** : Compile les fichiers sources du projet.
    - **Quand l'utiliser** : Utilisez cette commande pour vérifier que votre code source se compile correctement sans erreurs.
    - **Exemple** :
      ```sh
      mvn compile
      ```

3. **`mvn test`** :
    - **Utilisation** : Exécute les tests unitaires du projet.
    - **Quand l'utiliser** : Utilisez cette commande pour exécuter vos tests unitaires et vérifier que votre code fonctionne comme prévu.
    - **Exemple** :
      ```sh
      mvn test
      ```

4. **`mvn package`** :
    - **Utilisation** : Compile le code source, exécute les tests et empaquette le code compilé en un format redistribuable, comme un JAR ou un WAR.
    - **Quand l'utiliser** : Utilisez cette commande lorsque vous souhaitez créer un artefact redistribuable de votre application.
    - **Exemple** :
      ```sh
      mvn package
      ```

5. **`mvn install`** :
    - **Utilisation** : Installe l'artefact du package dans le dépôt local Maven, ce qui le rend disponible pour d'autres projets locaux.
    - **Quand l'utiliser** : Utilisez cette commande pour installer l'artefact dans votre dépôt Maven local. Cela est utile lorsque vous avez plusieurs projets Maven qui dépendent les uns des autres.
    - **Exemple** :
      ```sh
      mvn install
      ```

6. **`mvn clean install`** :
    - **Utilisation** : Combinaison des commandes `clean` et `install`. Supprime les fichiers générés par les builds précédents, compile le code source, exécute les tests, empaquette le code compilé et installe l'artefact dans le dépôt local Maven.
    - **Quand l'utiliser** : Utilisez cette commande pour effectuer un build complet à partir d'un état propre et installer l'artefact dans votre dépôt local Maven.
    - **Exemple** :
      ```sh
      mvn clean install
      ```

7. **`mvn spring-boot:run`** :
    - **Utilisation** : Démarre l'application Spring Boot.
    - **Quand l'utiliser** : Utilisez cette commande pour démarrer rapidement l'application Spring Boot sans avoir à empaqueter le code source.
    - **Exemple** :
      ```sh
      mvn spring-boot:run
      ```

### Scénarios Pratiques

1. **Développement Actif** :
    - **Commande** : `mvn spring-boot:run`
    - **Quand** : Lorsque vous travaillez activement sur le code et que vous voulez voir les changements immédiatement sans créer un JAR/WAR.

2. **Nettoyer le Répertoire de Build** :
    - **Commande** : `mvn clean`
    - **Quand** : Avant de lancer un nouveau build ou lorsque vous rencontrez des problèmes étranges de build.

3. **Vérifier la Compilation** :
    - **Commande** : `mvn compile`
    - **Quand** : Après avoir effectué des modifications significatives pour vérifier qu'elles se compilent sans erreur.

4. **Exécuter les Tests** :
    - **Commande** : `mvn test`
    - **Quand** : Après avoir ajouté ou modifié des tests pour vérifier qu'ils fonctionnent correctement.

5. **Créer un Artefact Redistribuable** :
    - **Commande** : `mvn package`
    - **Quand** : Lorsque vous souhaitez créer un JAR/WAR pour le déploiement.

6. **Installer l'Artefact dans le Dépôt Local** :
    - **Commande** : `mvn install`
    - **Quand** : Lorsque vous avez des projets locaux dépendants de cet artefact.

### Résumé

- **`mvn clean`** : Supprime les fichiers de build précédents.
- **`mvn dependency:purge-local-repository `** : nettoyer le dépôt local Maven
- **`mvn compile`** : Compile le code source.
- **`mvn test`** : Exécute les tests unitaires.
- **`mvn package`** : Crée un JAR/WAR redistribuable.
- **`mvn install`** : Installe l'artefact dans le dépôt local Maven.
- **`mvn spring-boot:run`** : Démarre l'application Spring Boot.

Bien sûr, je vais vous fournir un schéma récapitulatif de l'architecture de votre système d'authentification JWT, ainsi qu'une explication détaillée de comment les différentes classes et composants interagissent entre eux.

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

Ce schéma et cette explication devraient vous donner une vue d'ensemble claire de l'architecture de votre système d'authentification et de la manière dont les différentes classes interagissent entre elles pour fournir l'authentification et l'autorisation JWT dans votre application Spring Boot.