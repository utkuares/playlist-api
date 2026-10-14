package com.se4458.playlistapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class PositionRequest {

    @NotNull(message = "Position must not be null")
    @Min(value = 1, message = "Position must be at least 1")
    private Integer position;

    public PositionRequest() {}

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}