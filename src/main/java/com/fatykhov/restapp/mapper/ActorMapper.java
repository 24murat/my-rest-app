package com.fatykhov.restapp.mapper;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;

public class ActorMapper implements Mapper<Actor, ActorDto> {

    @Override
    public ActorDto entityToDto(Actor actor) {
        return ActorDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .build();
    }

    @Override
    public Actor dtoToEntity(ActorDto dto) {
        return Actor.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
