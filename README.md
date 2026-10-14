# Playlist API

A RESTful playlist management API built with Spring Boot. It lets you add, edit, remove, search, sort and reorder songs, and mark favorites.

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