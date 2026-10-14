package com.se4458.playlistapi.repository;

import com.se4458.playlistapi.model.Song;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SongRepository {

    private final ConcurrentHashMap<Long, Song> songs =
            new ConcurrentHashMap<>();

    private final AtomicLong idCounter = new AtomicLong(0);

    public List<Song> findAll() {
        return songs.values().stream()
                .sorted(Comparator.comparingInt(Song::getPosition)
                        .thenComparing(Song::getId))
                .toList();
    }

    public Optional<Song> findById(Long id) {
        Song song = songs.get(id);
        return Optional.ofNullable(song);
    }

    public Song save(Song song) {
        if (song.getId() == null) {
            song.setId(idCounter.incrementAndGet());
        }

        songs.put(song.getId(), song);

        return song;
    }

    public boolean deleteById(Long id) {
        Song song = songs.remove(id); // Even if the song is deleted, the song variable does not become null.
        return song != null;
    }
}