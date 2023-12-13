package com.fatykhov.restapp.service;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.ActorRepository;
import com.fatykhov.restapp.service.interfaces.ActorServiceInterface;

import java.util.List;

public class ActorService implements ActorServiceInterface {
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

    @Override
    public List<ActorDto> getAll() {
        List<Actor> actorList = actorRepository.findAll();
        return actorList.stream()
                .map(actorMapper::toDto)
                .toList();
    }

    @Override
    public ActorDto getById(long id) {
        return actorMapper.toDto(actorRepository.findOne(id));
    }

    public ActorDto save(ActorDto actorDto) {
        Actor actor = actorRepository.save(actorMapper.fromDto(actorDto));
        return actorMapper.toDto(actor);
    }

    @Override
    public ActorDto update(long id, ActorDto updatedActorDto) {
        Actor actor = actorRepository.update(id, actorMapper.fromDto(updatedActorDto));
        return actorMapper.toDto(actor);
    }

    @Override
    public boolean remove(long id) {
        return actorRepository.remove(id);
    }
}
