# Recipe Saver

A full-stack application that extracts and saves recipes from Instagram posts.

## Tech stack
- Frontend: React
- Backend: Spring Boot (Java)
- API: REST endpoints (Spring MVC)
- NLP: Server-side processing to extract ingredients and instructions from Instagram content

## Features
- Submit an Instagram URL and title to create a recipe
- Automatic extraction of ingredients and instructions via NLP
- Browse saved recipe cards in a responsive UI

## Getting started

### Prerequisites
- Java 11+ (or configured JDK used by Gradle)
- Gradle (wrapper included)
- Node.js and npm (for the frontend)

### Run the backend
From the project root run:

```bash
./gradlew bootRun
```

This starts the Spring Boot backend on the configured port (default: 8080).

### Run the frontend (development)

```bash
cd frontend
npm install
npm start
```

The React dev server runs on `http://localhost:3000` by default and can proxy API requests to the backend.

## API

- `GET /createRecipe?InstagramUrl=<url>&recipeTitle=<title>`
	- Triggers the NLP extraction process for the provided Instagram URL and returns a JSON recipe object containing `title`, `ingredients` (array of `{amount, ingredient}`), and `instructions` (array of strings).


## Project structure (high level)
- `frontend/` — React application (UI, components)
- `src/main/java/` — Spring Boot application code
- `src/main/resources/` — application properties
