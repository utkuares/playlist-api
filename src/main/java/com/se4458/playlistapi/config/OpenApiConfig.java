package com.se4458.playlistapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI playlistOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Playlist API")
                .version("1.0")
                .description("Simple REST API for managing a music playlist. "
                        + "Data is stored in memory and resets on restart."));
    }
}