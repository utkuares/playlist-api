# Playlist API

A RESTful playlist management API built with Spring Boot. It lets you add, edit, remove, search, sort and reorder songs, and mark favorites.

## Source Code

https://github.com/utkuares/playlist-api

## Live Demo

- API: https://playlist-api-7h4z.onrender.com/api/songs
- Swagger UI: https://playlist-api-7h4z.onrender.com/swagger-ui/index.html

> The app is hosted on Render's free plan. After a period of inactivity the first request can take up to a minute while the service wakes up.

## Tech Stack

- Java 21
- Spring Boot 4.1.1 (Spring Web MVC, Validation)
- springdoc-openapi (Swagger UI)
- Maven
- Docker
- Deployed on Render

## Endpoints

Base path: `/api/songs`

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/songs` | List songs. Optional query params: `search`, `genre`, `sortBy` (title, artist, duration), `order` (asc, desc) |
| GET | `/api/songs/{id}` | Get a song by id |
| GET | `/api/songs/summary` | Total song count and total duration |
| GET | `/api/songs/favorites` | List favorite songs |
| POST | `/api/songs` | Add a song |
| PUT | `/api/songs/{id}` | Edit a song |
| DELETE | `/api/songs/{id}` | Remove a song (remaining positions are renumbered) |
| PATCH | `/api/songs/{id}/favorite` | Mark or unmark a song as favorite |
| PATCH | `/api/songs/{id}/position` | Move a song to a new position |

### Example request body (POST / PUT)

```json
{
  "title": "Blinding Lights",
  "artist": "The Weeknd",
  "durationSeconds": 212,
  "genre": "Pop",
  "album": "After Hours",
  "coverUrl": "https://example.com/cover.jpg"
}
```

`title`, `artist`, `genre` and `durationSeconds` are required. `album` and `coverUrl` are optional.

## Error Handling

Errors are returned in a consistent JSON format:

| Status | Case |
|--------|------|
| 400 | Validation failure, malformed JSON, invalid parameter type (e.g. `/api/songs/abc`), invalid `sortBy` |
| 404 | Song not found |

## Run Locally

Requirements: Java 21 and Maven (or use the included Maven wrapper).

```bash
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## Run with Docker

```bash
docker build -t playlist-api .
docker run -p 8080:8080 playlist-api
```

## Notes

- Data is stored in memory and seeded with sample songs on startup. Changes are lost when the app restarts.
- The port can be overridden with the `PORT` environment variable (default 8080).

## Design

- Layered architecture: `controller` (REST endpoints), `service` (business logic), `repository` (in-memory storage), `dto` (request/response models), `exception` (global error handling) and `config` (OpenAPI and sample data seeding).
- Songs are stored in a `ConcurrentHashMap` inside `SongRepository`, so no external database is needed. `DataSeeder` loads 5 sample songs on startup.
- Request validation uses Bean Validation (`@NotBlank`, `@Positive`, `@Min`).
- `GlobalExceptionHandler` converts exceptions to a consistent JSON error response.
- Swagger UI is generated with springdoc-openapi, with descriptions and example values on every endpoint.

## Assumptions

- Positions start at 1 and are unique. When a song is removed, the remaining songs are renumbered.
- Moving a song to a new position shifts the other songs.
- `durationSeconds` must be greater than zero. `title`, `artist` and `genre` are required; `album` and `coverUrl` are optional.
- Search looks at title, artist and album.
- The genre filter is case-insensitive.
- Data is kept in memory only, so it resets when the app restarts.
- No authentication is required.

## Issues Encountered

- **Dockerfile:** IntelliJ created the Dockerfile from a sample template, so I replaced it with a multi-stage Maven + Java 21 build.
- **Port on Render:** Render assigns the port dynamically, so `server.port` is set to `${PORT:8080}`.
- **Error responses:** Malformed JSON and invalid ids (e.g. `/api/songs/abc`) first returned Spring's default error format. I added handlers so they return the same JSON error format as other errors.
- **Free hosting:** Render's free plan sleeps after inactivity, so the first request can be slow.
- **Git setup:** The project folder was not a Git repository at first, so I initialized it and pushed to GitHub.