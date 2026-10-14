
package com.se4458.playlistapi.exception;

public class SongNotFoundException extends RuntimeException {

    public SongNotFoundException(Long id) {
        super("Song not found with id: " + id);
    }

}