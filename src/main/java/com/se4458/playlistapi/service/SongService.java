package com.se4458.playlistapi.service;

import com.se4458.playlistapi.model.Song;
import com.se4458.playlistapi.repository.SongRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Locale;

import java.util.Optional;

import com.se4458.playlistapi.dto.PlaylistSummary;
import com.se4458.playlistapi.dto.SongRequest;

import com.se4458.playlistapi.exception.SongNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class SongService {

    private final SongRepository songRepository;

    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public PlaylistSummary getSummary() {
        List<Song> songs = songRepository.findAll();

        int totalSeconds = songs.stream()
                .mapToInt(Song::getDurationSeconds)
                .sum();

        String formatted = (totalSeconds / 60) + " min " + (totalSeconds % 60) + " sec";

        return new PlaylistSummary(songs.size(), totalSeconds, formatted);
    }

    public List<Song> searchSongs(String search, String genre,
                                  String sortBy, String order) {

        List<Song> result = songRepository.findAll();

        if (search != null && !search.isBlank()) {
            String keyword = search.trim().toLowerCase(Locale.ROOT);
            result = result.stream()
                    .filter(s -> contains(s.getTitle(), keyword)
                        || contains(s.getArtist(), keyword)
                        || contains(s.getAlbum(), keyword))
                    .toList();
        }

        if (genre != null && !genre.isBlank()) {
            result = result.stream()
                    .filter(s -> s.getGenre() != null
                            && s.getGenre().equalsIgnoreCase(genre.trim()))
                    .toList();
        }

        if (sortBy != null && !sortBy.isBlank()) {
            Comparator<Song> comparator = switch (sortBy.toLowerCase(Locale.ROOT)) {
                case "title" -> Comparator.comparing(
                        Song::getTitle, String.CASE_INSENSITIVE_ORDER);
                case "artist" -> Comparator.comparing(
                        Song::getArtist, String.CASE_INSENSITIVE_ORDER);
                case "duration" -> Comparator.comparingInt(Song::getDurationSeconds);
                default -> throw new IllegalArgumentException(
                        "Invalid sortBy value: " + sortBy
                                + ". Allowed values: title, artist, duration");
            };

            if ("desc".equalsIgnoreCase(order)) {
                comparator = comparator.reversed();
            }

            result = result.stream().sorted(comparator).toList();
        }

        return result;
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).orElseThrow(() -> new SongNotFoundException(id));
    }

    public Song createSong(SongRequest request) {
        Song song = new Song();
        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        song.setDurationSeconds(request.getDurationSeconds());
        song.setGenre(request.getGenre());
        song.setAlbum(request.getAlbum());
        song.setCoverUrl(request.getCoverUrl());
        song.setPosition(songRepository.findAll().size() + 1);

        return songRepository.save(song);
    }

    public Song updateSong(Long id, SongRequest request) {
        Song song = getSongById(id);

        song.setTitle(request.getTitle());
        song.setArtist(request.getArtist());
        song.setDurationSeconds(request.getDurationSeconds());
        song.setGenre(request.getGenre());
        song.setAlbum(request.getAlbum());
        song.setCoverUrl(request.getCoverUrl());

        return songRepository.save(song);
    }

    public List<Song> moveSong(Long id, int newPosition) {
        Song song = getSongById(id);

        List<Song> songs = new ArrayList<>(songRepository.findAll());

        if (newPosition < 1 || newPosition > songs.size()) {
            throw new IllegalArgumentException(
                    "Position must be between 1 and " + songs.size());
        }

        songs.remove(song);
        songs.add(newPosition - 1, song);

        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setPosition(i + 1);
            songRepository.save(songs.get(i));
        }

        return songRepository.findAll();
    }

    public void deleteSong(Long id) {
        boolean deleted = songRepository.deleteById(id);

        if (!deleted) {
            throw new SongNotFoundException(id);
        }

        normalizePositions();
    }

    private void normalizePositions() {
        List<Song> songs = songRepository.findAll();
        for (int i = 0; i < songs.size(); i++) {
            songs.get(i).setPosition(i + 1);
            songRepository.save(songs.get(i));
        }
    }

    public List<Song> getFavoriteSongs() {
        return songRepository.findAll()
                .stream()
                .filter(Song::isFavorite)
                .toList();
    }

    public Song setFavorite(Long id, boolean favorite) {
        Song song = getSongById(id);

        song.setFavorite(favorite);

        return songRepository.save(song);
    }

    private boolean contains(String value, String keyword) {
        return value != null
                && value.toLowerCase(Locale.ROOT).contains(keyword);
    }

}