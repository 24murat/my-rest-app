package com.fatykhov.restapi.util;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.entity.Movie;

public class TestObjectInitializer {

    public static ClientDto initializeClientDto() {
        return ClientDto.builder()
                .id(1L)
                .name("TestClient")
                .build();
    }

    public static Client initializeClient() {
        return Client.builder()
                .id(1L)
                .name("TestClient")
                .build();
    }

    public static MovieDto initializeMovieDto() {
        return MovieDto.builder()
                .id(1L)
                .clientId(2L)
                .title("TestMovie")
                .build();
    }

    public static Movie initializeMovie() {
        return Movie.builder()
                .id(1L)
                .clientId(2L)
                .title("TestMovie")
                .build();
    }

    public static ActorDto initializeActorDto() {
        return ActorDto.builder()
                .id(1L)
                .name("TestActor")
                .build();
    }

    public static Actor initializeActor() {
        return Actor.builder()
                .id(1L)
                .name("TestActor")
                .build();
    }
}