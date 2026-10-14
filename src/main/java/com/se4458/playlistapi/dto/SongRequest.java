package com.se4458.playlistapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

public class SongRequest {

    @Schema(example = "Blinding Lights")
    @NotBlank(message = "Title must not be blank")
    private String title;

    @Schema(example = "The Weeknd")
    @NotBlank(message = "Artist must not be blank")
    private String artist;

    @Schema(example = "212", description = "Duration in seconds")
    @Positive(message = "Duration must be greater than zero")
    private int durationSeconds;

    @Schema(example = "Pop")
    @NotBlank(message = "Genre must not be blank")
    private String genre;

    // Optional fields
    @Schema(example = "After Hours")
    private String album;

    @Schema(example = "https://example.com/cover.jpg")
    private String coverUrl;

    public SongRequest() {}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getDurationSeconds() {
        return durationSeconds;
    }

    public void setDurationSeconds(int durationSeconds) {
        this.durationSeconds = durationSeconds;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}