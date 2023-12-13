package com.fatykhov.restapp.service;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.ActorRepository;

import java.util.List;

public class ActorService {
    private final ActorRepository actorRepository;
    private final Mapper<Actor, ActorDto> actorMapper;

    public ActorService() {
        this.actorRepository = new ActorRepository();
        this.actorMapper = new ActorMapper();
    }

    public ActorService(ActorRepository actorRepository, Mapper<Actor, ActorDto> actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    public List<ActorDto> getAllActors() {
        List<Actor> actorList = actorRepository.findAll();
        return actorList.stream()
                .map(actorMapper::entityToDto)
                .toList();
    }

    public ActorDto getActorById(long id) {
        return actorMapper.entityToDto(actorRepository.findOne(id));
    }

    public ActorDto saveActor(ActorDto actorDto) {
        Actor actor = actorRepository.save(actorMapper.dtoToEntity(actorDto));
        return actorMapper.entityToDto(actor);
    }

    public ActorDto updateActor(long id, ActorDto updatedActorDto) {
        Actor actor = actorRepository.update(id, actorMapper.dtoToEntity(updatedActorDto));
        return actorMapper.entityToDto(actor);
    }

    public boolean removeActor(long id) {
        return actorRepository.remove(id);
    }
}
