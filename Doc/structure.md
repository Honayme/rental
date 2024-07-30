### Schéma des Interactions

```
     +-------------------+
     |    Controller     |
     +-------------------+
              |
              v
     +-------------------+
     |       DTO         |
     +-------------------+
              |
              v
     +-------------------+
     |     Service       |
     +-------------------+
              |
              v
     +-------------------+
     |   Repository      |
     +-------------------+
              |
              v
     +-------------------+
     |    Entities       |
     +-------------------+
              |
              v
     +-------------------+
     |  Database Layer   |
     +-------------------+
```

### Explications des Packages et de leurs Rôles

1. **Controller**:
    - **Rôle**: Gérer les requêtes HTTP entrantes, traiter les entrées utilisateur, et retourner des réponses HTTP.
    - **Interaction**: Les contrôleurs reçoivent les requêtes, utilisent les services pour traiter la logique métier et renvoient les résultats aux clients.

2. **DTO (Data Transfer Object)**:
    - **Rôle**: Encapsuler les données transférées entre les couches de l'application, notamment entre le client et le serveur.
    - **Interaction**: Les DTOs sont utilisés par les contrôleurs pour recevoir et envoyer des données, séparant ainsi la structure des entités internes des structures utilisées pour les échanges de données.

3. **Service**:
    - **Rôle**: Contient la logique métier de l'application. Il orchestre les appels aux différents composants du système.
    - **Interaction**: Les services utilisent les repositories pour interagir avec la base de données et appliquent la logique métier sur les données récupérées.

4. **Repository**:
    - **Rôle**: Interagir directement avec la base de données. Effectuer les opérations CRUD (Create, Read, Update, Delete).
    - **Interaction**: Les repositories sont utilisés par les services pour accéder et manipuler les données dans la base de données.

5. **Entities**:
    - **Rôle**: Représentent les objets de la base de données sous forme de classes Java. Chaque entité est liée à une table de la base de données.
    - **Interaction**: Les entités sont manipulées par les repositories et les services pour représenter et gérer les données persistées.

6. **Config**:
    - **Rôle**: Contient les classes de configuration de l'application, comme la configuration de sécurité, les beans, les sources de données, etc.
    - **Interaction**: Les classes de configuration définissent comment les autres composants de l'application sont configurés et interagissent les uns avec les autres.

### Exemple détaillé des interactions

1. **Requête HTTP**:
    - Une requête HTTP est envoyée par le client (par exemple, une requête POST pour créer une nouvelle entité).

2. **Controller**:
    - Le contrôleur reçoit la requête HTTP et extrait les données de la requête.
    - Il utilise un DTO pour encapsuler les données de la requête et appelle un service avec ce DTO.

3. **Service**:
    - Le service reçoit le DTO et applique la logique métier nécessaire.
    - Si des données doivent être persistées, le service appelle le repository approprié avec une entité.

4. **Repository**:
    - Le repository effectue les opérations CRUD nécessaires sur la base de données.
    - Il renvoie les entités mises à jour ou récupérées au service.

5. **Service**:
    - Le service transforme les entités en DTOs si nécessaire et les renvoie au contrôleur.

6. **Controller**:
    - Le contrôleur transforme les DTOs en réponses HTTP et les renvoie au client.
