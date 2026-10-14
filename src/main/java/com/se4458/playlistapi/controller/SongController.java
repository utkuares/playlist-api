package com.se4458.playlistapi.controller;

import com.se4458.playlistapi.model.Song;
import com.se4458.playlistapi.service.SongService;
import com.se4458.playlistapi.dto.SongRequest;
import com.se4458.playlistapi.dto.FavoriteRequest;
import com.se4458.playlistapi.dto.PlaylistSummary;
import com.se4458.playlistapi.dto.PositionRequest;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation; // Describes what an endpoint does.
import io.swagger.v3.oas.annotations.Parameter; // Describes a parameter and provides an example value.
import io.swagger.v3.oas.annotations.responses.ApiResponse; // Documents the possible HTTP responses of an API.
import io.swagger.v3.oas.annotations.tags.Tag; // Groups endpoints in Swagger UI.

@RestController
@RequestMapping("/api/songs")
@Tag(name = "Songs", description = "Playlist song management")
// @Tag groups all endpoints in a controller under a single category in Swagger UI.
public class SongController {
    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @Operation(
            summary = "List songs",
            description = "Returns the playlist with search, genre filter and sorting."
    )
    @ApiResponse(responseCode = "200", description = "Songs returned")
    @ApiResponse(responseCode = "400", description = "Invalid sortBy value")
    @GetMapping
    public List<Song> getAllSongs(
            @Parameter(description = "Search in title, artist or album", example = "weeknd")
            @RequestParam(required = false) String search,
            @Parameter(description = "Filter by genre (case-insensitive)", example = "Pop")
            @RequestParam(required = false) String genre,
            @Parameter(description = "Sort field: title, artist or duration", example = "title")
            @RequestParam(required = false) String sortBy,
            @Parameter(description = "Sort order: asc or desc", example = "asc")
            @RequestParam(required = false, defaultValue = "asc") String order) {
        return songService.searchSongs(search, genre, sortBy, order);
    }

    @Operation(summary = "Get a song by id")
    @ApiResponse(responseCode = "200", description = "Song found")
    @ApiResponse(responseCode = "404", description = "Song not found")
    @GetMapping("/{id}")
    public Song getSongById(@PathVariable Long id) {
        return songService.getSongById(id);
    }


    @Operation(summary = "Get playlist summary",
            description = "Returns the total number of songs and the total duration.")
    @GetMapping("/summary")
    public PlaylistSummary getSummary() {
        return songService.getSummary();
    }

    @Operation(summary = "Add a song",
            description = "Adds a new song to the end of the playlist.")
    @ApiResponse(responseCode = "201", description = "Song created")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Song createSong(@Valid @RequestBody SongRequest request) {
        return songService.createSong(request);
    }

    @Operation(summary = "Edit a song",
            description = "Updates title, artist, album, duration, genre and cover of a song.")
    @ApiResponse(responseCode = "200", description = "Song updated")
    @ApiResponse(responseCode = "400", description = "Validation failed")
    @ApiResponse(responseCode = "404", description = "Song not found")
    @PutMapping("/{id}")
    public Song updateSong(@PathVariable Long id,
                           @Valid @RequestBody SongRequest request) {
        return songService.updateSong(id, request);
    }


    @Operation(summary = "Remove a song",
            description = "Removes the song and renumbers the remaining positions.")
    @ApiResponse(responseCode = "204", description = "Song removed")
    @ApiResponse(responseCode = "404", description = "Song not found")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }

    /**
     @DeleteMapping("/{id}")
     @ResponseStatus(HttpStatus.NO_CONTENT)
     public void deleteSong(@PathVariable Long id) {
     songService.deleteSong(id);
     }
     **/

    @Operation(summary = "Mark or unmark a song as favorite")
    @ApiResponse(responseCode = "200", description = "Favorite status updated")
    @ApiResponse(responseCode = "404", description = "Song not found")
    @PatchMapping("/{id}/favorite")
    public Song setFavorite(@PathVariable Long id,
                            @Valid @RequestBody FavoriteRequest request) {
        return songService.setFavorite(id, request.getFavorite());
    }

    @Operation(summary = "Move a song to a new position",
            description = "Reorders the playlist (drag and drop). Position starts from 1.")
    @ApiResponse(responseCode = "200", description = "Updated playlist returned")
    @ApiResponse(responseCode = "400", description = "Position out of range")
    @ApiResponse(responseCode = "404", description = "Song not found")
    @PatchMapping("/{id}/position")
    public List<Song> moveSong(@PathVariable Long id,
                               @Valid @RequestBody PositionRequest request) {
        return songService.moveSong(id, request.getPosition());
    }

    @Operation(summary = "List favorite songs")
    @GetMapping("/favorites")
    public List<Song> getFavoriteSongs() {
        return songService.getFavoriteSongs();
    }
    
}