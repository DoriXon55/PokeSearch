# PokeSearch

PokeSearch is a web application built in Java using Spring Boot that allows users to search for Pokémon, create their own Pokémon teams, and manage their favorite Pokémon. The application integrates with an external API (PokeAPI) to fetch Pokémon data.

## Main Features

*   **Pokémon Search**: Users can search for Pokémon by their name or ID.
*   **Pokémon Details**: Displays detailed information about Pokémon.
*   **Pokémon Listing**: Browse a list of Pokémon with pagination.
*   **Pokémon Team Management**:
    *   Create new teams.
    *   Add Pokémon to teams (maximum 6 per team, each in a specific position).
    *   Remove Pokémon from teams.
    *   View user's teams.
    *   Update and delete teams.
*   **Favorite Pokémon Management**:
    *   Add Pokémon to a favorites list.
    *   Remove Pokémon from a favorites list.
    *   View user's list of favorite Pokémon.
*   **Authentication and Authorization**: JWT-based system to secure access to user functionalities.
*   **User Registration**: New users can create accounts.
*   **Email Sending**: Email notifications (e.g., after registration), configured to work with Mailtrap.
*   **Caching**: Utilizes caching mechanisms (e.g., `@Cacheable`) to optimize API and database queries.

## Technologies

*   **Java**
*   **Spring Boot**:
    *   Spring Web
    *   Spring Data JPA
    *   Spring Security
    *   Spring Cache
*   **Hibernate** (as JPA implementation)
*   **MySQL** (database)
*   **Maven** (dependency management and project building)
*   **RestTemplate** (for communication with the external API)
*   **Lombok** (to reduce boilerplate code - *similar to the above, a common addition*)

## Project Structure (main components)

*   **`com.project.pokesearch.controller`**: REST controllers handling HTTP requests.
    *   `AuthController`: User registration and login.
    *   `FavoritePokemonController`: Manages favorite Pokémon.
    *   `PokemonController`: Searches and retrieves Pokémon information from PokeAPI.
    *   `TeamController`: Manages Pokémon teams.
    *   `TeamPokemonController`: Manages Pokémon within teams.
*   **`com.project.pokesearch.service`**: Business logic of the application.
    *   `PokemonService`: Communicates with PokeAPI, fetches and caches Pokémon data.
    *   `TeamService`: Operations on teams.
    *   `TeamPokemonService`: Operations on Pokémon in teams.
    *   `FavoritePokemonService`: Operations on favorite Pokémon.
    *   `UserService`: Manages users.
    *   `EmailService`: Sends emails.
*   **`com.project.pokesearch.repository`**: Spring Data JPA interfaces for database interaction.
    *   `UserRepository`
    *   `TeamRepository`
    *   `TeamPokemonRepository`
    *   `FavoritePokemonRepository`
*   **`com.project.pokesearch.model`**: JPA entities representing the data structure.
    *   `User`
    *   `Team`
    *   `TeamPokemon`
    *   `FavoritePokemon`
*   **`com.project.pokesearch.dto`**: Data Transfer Objects used for transferring data between layers (mainly in API requests/responses).
*   **`com.project.pokesearch.config`**: Application configuration (e.g., security, JWT, cache).
*   **`com.project.pokesearch.security.jwt`**: Components related to JWT handling (e.g., `JwtUtils`, `AuthTokenFilter`).
*   **`com.project.pokesearch.mapper`**: Mapping between entities and DTOs (e.g., `UserMapper`).

## Configuration

Before running the application, you need to configure the database connection and other settings.

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/DoriXon55/PokeSearch.git
    cd PokeSearch
    ```

2.  **Configure the `application.properties` file:**
    The file `src/main/resources/application.properties.example` contains an example configuration. Copy this file to `src/main/resources/application.properties` and fill it with the appropriate data:
    ```bash
    cp src/main/resources/application.properties.example src/main/resources/application.properties
    ```
    Open the `application.properties` file and modify the following values:

    *   **MySQL Database Configuration:**
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/pokesearch # or your database URL
        spring.datasource.username=your_mysql_username
        spring.datasource.password=your_mysql_password
        ```
        Ensure the `pokesearch` database exists.

    *   **JWT Configuration:**
        ```properties
        app.jwt.secret=your_jwt_secret_key # Generate a strong, random key
        app.jwt.expirationMs=86400000 # Token expiration time in milliseconds (e.g., 24 hours)
        ```

    *   **Mailtrap Configuration (for testing email sending):**
        ```properties
        spring.mail.host=sandbox.smtp.mailtrap.io
        spring.mail.port=587 # or another port if you use a different SMTP server
        spring.mail.username=your_mailtrap_username
        spring.mail.password=your_mailtrap_password
        spring.mail.properties.mail.smtp.auth=true
        spring.mail.properties.mail.smtp.starttls.enable=true
        ```

3.  **Building and running the application:**
    The application can be run using Maven:
    ```bash
    mvn spring-boot:run
    ```
    Or after building the `.jar` package:
    ```bash
    mvn clean package
    java -jar target/PokeSearch-0.0.1-SNAPSHOT.jar # The JAR file name may vary
    ```

    The application will be available by default at `http://localhost:8080`.

## API Endpoints

Main API endpoints (examples):

*   `POST /api/auth/signup` - Register a new user
*   `POST /api/auth/signin` - Log in a user
*   `GET /api/pokemon?limit={limit}&offset={offset}` - List of Pokémon
*   `GET /api/pokemon/search/{nameOrId}` - Search for a Pokémon
*   `GET /api/pokemon/{id}` - Pokémon details
*   `GET /api/teams` - List of the logged-in user's teams
*   `POST /api/teams` - Create a new team
*   `GET /api/teams/{teamId}` - Team details
*   `PUT /api/teams/{teamId}` - Update a team
*   `DELETE /api/teams/{teamId}` - Delete a team
*   `GET /api/teams/{teamId}/pokemons` - List of Pokémon in a team
*   `POST /api/teams/{teamId}/pokemons` - Add a Pokémon to a team
*   `DELETE /api/teams/{teamId}/pokemons/{position}` - Remove a Pokémon from a team at a given position
*   `GET /api/favorites` - List of the logged-in user's favorite Pokémon
*   `POST /api/favorites` - Add a Pokémon to favorites
*   `DELETE /api/favorites/{pokemonId}` - Remove a Pokémon from favorites

*(The exact list of endpoints and request/response details might be available, for example, via Swagger/OpenAPI)*

## How to Contribute

If you want to contribute to the project's development:

1.  Fork the repository.
2.  Create a new branch (`git checkout -b feature/your-feature`).
3.  Make your changes.
4.  Commit your changes (`git commit -am 'Add new feature'`).
5.  Push the changes to the branch (`git push origin feature/your-feature`).
6.  Create a Pull Request.

---
**Acknowledgements:** This application uses data provided by [PokeAPI](https://pokeapi.co/). Thank you to the creators of PokeAPI for their hard work and for making this fantastic resource available!
