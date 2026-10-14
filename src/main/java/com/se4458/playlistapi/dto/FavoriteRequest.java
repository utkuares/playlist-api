package com.se4458.playlistapi.dto;

import jakarta.validation.constraints.NotNull;

public class FavoriteRequest {

    @NotNull(message = "Favorite cannot be null")
    private Boolean favorite;

    public FavoriteRequest() {
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }
}