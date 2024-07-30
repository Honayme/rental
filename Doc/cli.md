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