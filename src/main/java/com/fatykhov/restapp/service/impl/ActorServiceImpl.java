package com.fatykhov.restapp.service.impl;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.impl.ActorRepositoryImpl;
import com.fatykhov.restapp.service.ActorService;

import java.util.List;

public class ActorServiceImpl implements ActorService {
    private final ActorRepositoryImpl actorRepositoryImpl;
    private final Mapper<Actor, ActorDto> actorMapper;

    public ActorServiceImpl() {
        this.actorRepositoryImpl = new ActorRepositoryImpl();
        this.actorMapper = new ActorMapper();
    }

    public ActorServiceImpl(ActorRepositoryImpl actorRepositoryImpl, Mapper<Actor, ActorDto> actorMapper) {
        this.actorRepositoryImpl = actorRepositoryImpl;
        this.actorMapper = actorMapper;
    }

    @Override
    public List<ActorDto> getAll() {
        List<Actor> actorList = actorRepositoryImpl.findAll();
        return actorList.stream()
                .map(actorMapper::toDto)
                .toList();
    }

    @Override
    public ActorDto getById(long id) {
        return actorMapper.toDto(actorRepositoryImpl.findOne(id));
    }

    public ActorDto save(ActorDto actorDto) {
        Actor actor = actorRepositoryImpl.save(actorMapper.fromDto(actorDto));
        return actorMapper.toDto(actor);
    }

    @Override
    public ActorDto update(long id, ActorDto updatedActorDto) {
        Actor actor = actorRepositoryImpl.update(id, actorMapper.fromDto(updatedActorDto));
        return actorMapper.toDto(actor);
    }

    @Override
    public boolean remove(long id) {
        return actorRepositoryImpl.remove(id);
    }
}
