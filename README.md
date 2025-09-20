# Research Articles Management Application

This project is a web application designed to manage personal research articles. It utilizes a Java Spring Boot backend following the MVC pattern and a React.js frontend.

## Project Structure

The project is divided into two main parts: the backend and the frontend.

### Backend

The backend is built using Spring Boot and follows the MVC architecture. It provides RESTful APIs for managing articles.

- **Entry Point**: `backend/src/main/java/com/example/researcharticles/ResearchArticlesApplication.java`
- **Controller**: `backend/src/main/java/com/example/researcharticles/controller/ArticleController.java`
- **Model**: `backend/src/main/java/com/example/researcharticles/model/Article.java`
- **Repository**: `backend/src/main/java/com/example/researcharticles/repository/ArticleRepository.java`
- **Service**: `backend/src/main/java/com/example/researcharticles/service/ArticleService.java`
- **Configuration**: `backend/src/main/resources/application.properties`
- **Dependencies**: `backend/pom.xml`

### Frontend

The frontend is built using React.js and provides a user-friendly interface for interacting with the articles.

- **Main Component**: `frontend/src/App.jsx`
- **Article List Component**: `frontend/src/components/ArticleList.jsx`
- **Article Form Component**: `frontend/src/components/ArticleForm.jsx`
- **Article Detail Component**: `frontend/src/components/ArticleDetail.jsx`
- **Entry Point**: `frontend/src/index.js`
- **HTML Template**: `frontend/public/index.html`
- **Dependencies**: `frontend/package.json`

## Features

- Create, update, and delete articles.
- View a list of all articles.
- View details of a specific article.

## Getting Started

### Prerequisites

- Java 11 or higher
- Node.js and npm
- Maven

### Backend Setup

1. Navigate to the `backend` directory.
2. Run `mvn spring-boot:run` to start the Spring Boot application.

### Frontend Setup

1. Navigate to the `frontend` directory.
2. Run `npm install` to install dependencies.
3. Run `npm start` to start the React application.

## License

This project is licensed under the MIT License.