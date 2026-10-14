
package com.se4458.playlistapi;

import com.se4458.playlistapi.repository.SongRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.se4458.playlistapi.model.Song;

class SongRepositoryTest {

    @Test
    void repositoryShouldStartEmpty() {
        SongRepository repository = new SongRepository();

        assertTrue(repository.findAll().isEmpty());
    }


    @Test
    void shouldSaveAndFindSong() {
        SongRepository repository = new SongRepository();

        Song song = new Song();
        song.setTitle("Test Song");
        song.setArtist("Test Artist");
        song.setDurationSeconds(180);
        song.setGenre("Rock");
        song.setFavorite(true);

        Song savedSong = repository.save(song);

        assertNotNull(savedSong.getId());
        assertEquals("Test Song", repository.findById(savedSong.getId())
                .orElseThrow()
                .getTitle());
    }

    @Test
    void shouldReturnEmptyWhenSongDoesNotExist() {
        SongRepository repository = new SongRepository();

        assertTrue(repository.findById(999L).isEmpty());
    }

    @Test
    void shouldDeleteSongAndReturnFalseWhenDeletedAgain() {
        SongRepository repository = new SongRepository();

        Song song = new Song();
        song.setTitle("Delete Test");
        song.setArtist("Test Artist");

        Song savedSong = repository.save(song);
        Long id = savedSong.getId();

        assertTrue(repository.deleteById(id));
        assertFalse(repository.deleteById(id));
        assertTrue(repository.findById(id).isEmpty());
    }

}
