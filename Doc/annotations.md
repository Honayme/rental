### Annotations Principales de Spring Boot

#### @SpringBootApplication
- Indique une classe principale qui démarre une application Spring Boot.
- Usage : Classe principale de l'application.
- Exemple :
  ```java
  @SpringBootApplication
  public class Application {
      public static void main(String[] args) {
          SpringApplication.run(Application.class, args);
      }
  }

#### @Bean
- Produit un bean à gérer par le conteneur Spring.
- Usage : Méthode.
- Exemple :
  ```java
  @Bean
  public MyService myService() {
      return new MyServiceImpl();
  }
  ```

#### @Autowired
- Injecte automatiquement les dépendances requises par un bean.
- Usage : Constructeur, champ, méthode setter.
- Exemple :
  ```java
  @Autowired
  private MyService myService;
  ```

#### @Component
- Indique qu'une classe est un composant Spring.
- Usage : Classe.
- Exemple :
  ```java
  @Component
  public class MyComponent {
  }
  ```

#### @Service
- Spécialisation de `@Component` indiquant qu'une classe est un service.
- Usage : Classe.
- Exemple :
  ```java
  @Service
  public class MyService {
  }
  ```

#### @Repository
- Spécialisation de `@Component` indiquant qu'une classe est un DAO.
- Usage : Classe.
- Exemple :
  ```java
  @Repository
  public class MyRepository {
  }
  ```

#### @Controller
- Spécialisation de `@Component` indiquant qu'une classe est un contrôleur Spring MVC.
- Usage : Classe.
- Exemple :
  ```java
  @Controller
  public class MyController {
  }
  ```

#### @RestController
- Combinaison de `@Controller` et `@ResponseBody`.
- Usage : Classe.
- Exemple :
  ```java
  @RestController
  public class MyRestController {
  }
  ```

#### @RequestMapping
- Associe une méthode ou une classe à une URL.
- Usage : Classe, méthode.
- Exemple :
  ```java
  @RequestMapping("/api")
  public class MyController {
      @RequestMapping("/hello")
      public String sayHello() {
          return "Hello, World!";
      }
  }
  ```

#### @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- Spécialisation de `@RequestMapping` pour les méthodes HTTP.
- Usage : Méthode.
- Exemple :
  ```java
  @GetMapping("/users")
  public List<User> getUsers() {
      return userService.getAllUsers();
  }
  ```

#### @PathVariable
- Extrait des valeurs de variables d'URI.
- Usage : Paramètre de méthode.
- Exemple :
  ```java
  @GetMapping("/users/{id}")
  public User getUserById(@PathVariable Long id) {
      return userService.getUserById(id);
  }
  ```

#### @RequestParam
- Extrait des paramètres de requête.
- Usage : Paramètre de méthode.
- Exemple :
  ```java
  @GetMapping("/search")
  public List<User> searchUsers(@RequestParam String name) {
      return userService.searchUsersByName(name);
  }
  ```

#### @RequestBody
- Extrait le corps de la

requête JSON et le convertit en objet Java.
- Usage : Paramètre de méthode.
- Exemple :
  ```java
  @PostMapping("/users")
  public User createUser(@RequestBody User user) {
      return userService.createUser(user);
  }
  ```

#### @ResponseBody
- Indique que le retour d'une méthode doit être écrit directement dans le corps de la réponse HTTP.
- Usage : Méthode.
- Exemple :
  ```java
  @GetMapping("/hello")
  @ResponseBody
  public String sayHello() {
      return "Hello, World!";
  }
  ```

#### @Configuration
- Indique qu'une classe déclare des méthodes de configuration Spring.
- Usage : Classe.
- Exemple :
  ```java
  @Configuration
  public class AppConfig {
      @Bean
      public MyService myService() {
          return new MyServiceImpl();
      }
  }
  ```

#### @EnableWebSecurity
- Active la sécurité web Spring.
- Usage : Classe de configuration.
- Exemple :
  ```java
  @Configuration
  @EnableWebSecurity
  public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  }
  ```
