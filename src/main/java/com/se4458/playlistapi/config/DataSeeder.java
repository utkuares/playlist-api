package com.se4458.playlistapi.config;

import com.se4458.playlistapi.model.Song;
import com.se4458.playlistapi.repository.SongRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner { // CommandLineRunner allows the run() method to execute
    // automatically when the Spring Boot application starts.

    private final SongRepository songRepository;

    public DataSeeder(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    @Override ///
    public void run(String... args) {
        if (!songRepository.findAll().isEmpty()) {
            return;
        }

        add("Blinding Lights", "The Weeknd", "After Hours", 212, "Pop", true, 1);
        add("Levitating", "Dua Lipa", "Future Nostalgia", 220, "Pop", false, 2);
        add("Watermelon Sugar", "Harry Styles", "Fine Line", 214, "Pop", true, 3);
        add("Save Your Tears", "The Weeknd", "After Hours", 246, "R&B", false, 4);
        add("Peaches", "Justin Bieber", "Justice", 154, "Pop", true, 5);
    }

    private void add(String title, String artist, String album,
                     int durationSeconds, String genre,
                     boolean favorite, int position) {
        Song song = new Song();
        song.setTitle(title);
        song.setArtist(artist);
        song.setAlbum(album);
        song.setDurationSeconds(durationSeconds);
        song.setGenre(genre);
        song.setFavorite(favorite);
        song.setPosition(position);
        songRepository.save(song);
    }
}