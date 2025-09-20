# Research Articles Management Application

This project is a web application designed to manage personal research articles. It utilizes a Java Spring Boot backend following the MVC pattern and a React.js frontend.

## Project Structure

The project is divided into two main parts: the backend and the frontend.

### Backend

The backend is built using Spring Boot and follows the MVC architecture. It provides RESTful APIs for managing articles.

- **ResearchArticlesApplication.java**: The entry point of the Spring Boot application.
- **ArticleController.java**: Handles HTTP requests related to articles (create, update, delete, get).
- **Article.java**: Represents the article entity with properties such as id, title, content, and author.
- **ArticleRepository.java**: Interface for CRUD operations on Article entities.
- **ArticleService.java**: Contains business logic for managing articles.
- **application.properties**: Configuration properties for the Spring Boot application.

### Frontend

The frontend is built using React.js and provides a user interface for interacting with the backend.

- **ArticleList.jsx**: Displays a list of articles.
- **ArticleForm.jsx**: Provides a form for creating and updating articles.
- **ArticleDetail.jsx**: Displays the details of a single article.
- **App.jsx**: Main component that sets up routing and includes other components.
- **index.js**: Entry point of the React application.
- **index.html**: Main HTML file for the React application.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- Node.js and npm

### Backend Setup

1. Navigate to the `backend` directory.
2. Run the following command to build the backend application:
   ```
   mvn clean install
   ```
3. Start the Spring Boot application:
   ```
   mvn spring-boot:run
   ```

### Frontend Setup

1. Navigate to the `frontend` directory.
2. Install the required dependencies:
   ```
   npm install
   ```
3. Start the React application:
   ```
   npm start
   ```

## API Endpoints

- `POST /api/articles`: Create a new article.
- `PUT /api/articles/{id}`: Update an existing article.
- `DELETE /api/articles/{id}`: Delete an article.
- `GET /api/articles`: Retrieve all articles.

## License

This project is licensed under the MIT License.