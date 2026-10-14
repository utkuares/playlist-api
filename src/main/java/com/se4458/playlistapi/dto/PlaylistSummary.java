package com.se4458.playlistapi.dto;

public record PlaylistSummary(
        int totalSongs,
        int totalDurationSeconds,
        String formattedDuration
) {}