package com.se4458.playlistapi;

import com.se4458.playlistapi.dto.SongRequest;
import com.se4458.playlistapi.exception.SongNotFoundException;
import com.se4458.playlistapi.model.Song;
import com.se4458.playlistapi.repository.SongRepository;
import com.se4458.playlistapi.service.SongService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SongServiceTest {

    private SongRequest request(String title) {
        SongRequest r = new SongRequest();
        r.setTitle(title);
        r.setArtist("Artist");
        r.setDurationSeconds(200);
        r.setGenre("Pop");
        r.setAlbum("Album");
        return r;
    }

    @Test
    void createSongShouldAssignIncreasingPositions() {
        SongService service = new SongService(new SongRepository());

        Song a = service.createSong(request("A"));
        Song b = service.createSong(request("B"));

        assertEquals(1, a.getPosition());
        assertEquals(2, b.getPosition());
        assertEquals("Album", a.getAlbum());
    }

    @Test
    void deleteSongShouldRenumberRemainingSongs() {
        SongService service = new SongService(new SongRepository());

        service.createSong(request("A"));
        Song b = service.createSong(request("B"));
        service.createSong(request("C"));

        service.deleteSong(b.getId());

        List<Song> songs = service.getAllSongs();
        assertEquals(2, songs.size());
        assertEquals("A", songs.get(0).getTitle());
        assertEquals(1, songs.get(0).getPosition());
        assertEquals("C", songs.get(1).getTitle());
        assertEquals(2, songs.get(1).getPosition());
    }

    @Test
    void deleteUnknownSongShouldThrow() {
        SongService service = new SongService(new SongRepository());

        assertThrows(SongNotFoundException.class, () -> service.deleteSong(99L));
    }

    @Test
    void shouldReturnSongsOrderedByPosition() {
        SongRepository repository = new SongRepository();

        Song third = new Song();
        third.setTitle("C");
        third.setPosition(3);
        Song first = new Song();
        first.setTitle("A");
        first.setPosition(1);
        Song second = new Song();
        second.setTitle("B");
        second.setPosition(2);

        repository.save(third);
        repository.save(first);
        repository.save(second);

        var titles = repository.findAll().stream().map(Song::getTitle).toList();

        assertEquals(java.util.List.of("A", "B", "C"), titles);
    }

    @Test
    void searchShouldFilterByKeywordAndGenre() {
        SongService service = new SongService(new SongRepository());

        SongRequest a = request("Blinding Lights");
        a.setArtist("The Weeknd");
        SongRequest b = request("Peaches");
        b.setArtist("Justin Bieber");
        b.setGenre("R&B");
        service.createSong(a);
        service.createSong(b);

        assertEquals(1, service.searchSongs("weeknd", null, null, "asc").size());
        assertEquals(1, service.searchSongs(null, "r&b", null, "asc").size());
        assertEquals(0, service.searchSongs("weeknd", "R&B", null, "asc").size());
    }

    @Test
    void searchShouldSortByTitleDescending() {
        SongService service = new SongService(new SongRepository());

        service.createSong(request("A"));
        service.createSong(request("C"));
        service.createSong(request("B"));

        var titles = service.searchSongs(null, null, "title", "desc")
                .stream().map(Song::getTitle).toList();

        assertEquals(List.of("C", "B", "A"), titles);
    }

    @Test
    void searchWithInvalidSortByShouldThrow() {
        SongService service = new SongService(new SongRepository());

        assertThrows(IllegalArgumentException.class,
                () -> service.searchSongs(null, null, "abc", "asc"));
    }

    @Test
    void summaryShouldCalculateTotalsCorrectly() {
        SongService service = new SongService(new SongRepository());

        SongRequest a = request("A");
        a.setDurationSeconds(212);
        SongRequest b = request("B");
        b.setDurationSeconds(834);
        service.createSong(a);
        service.createSong(b);

        var summary = service.getSummary();

        assertEquals(2, summary.totalSongs());
        assertEquals(1046, summary.totalDurationSeconds());
        assertEquals("17 min 26 sec", summary.formattedDuration());
    }

    @Test
    void moveSongShouldReorderPlaylist() {
        SongService service = new SongService(new SongRepository());

        service.createSong(request("A"));
        service.createSong(request("B"));
        Song c = service.createSong(request("C"));

        List<Song> result = service.moveSong(c.getId(), 1);

        assertEquals(List.of("C", "A", "B"),
                result.stream().map(Song::getTitle).toList());
        assertEquals(List.of(1, 2, 3),
                result.stream().map(Song::getPosition).toList());
    }

    @Test
    void moveSongToInvalidPositionShouldThrow() {
        SongService service = new SongService(new SongRepository());

        Song a = service.createSong(request("A"));

        assertThrows(IllegalArgumentException.class,
                () -> service.moveSong(a.getId(), 5));
    }

    @Test
    void moveUnknownSongShouldThrowNotFound() {
        SongService service = new SongService(new SongRepository());

        assertThrows(SongNotFoundException.class,
                () -> service.moveSong(99L, 1));
    }
}