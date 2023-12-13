package com.fatykhov.restapi.mapper;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActorMapperTest {

    @Mock
    private ActorMapper actorMapper;

    private ActorDto expectedActorDto;
    private Actor expectedActor;

    @BeforeEach
    void setUp() {
        expectedActorDto = ActorDto.builder()
                .id(1L)
                .name("TestActor")
                .build();

        expectedActor = Actor.builder()
                .id(1L)
                .name("TestActor")
                .build();
    }

    @Test
    void entityToDtoTest() {
        when(actorMapper.entityToDto(expectedActor)).thenReturn(expectedActorDto);
        ActorDto actorDto = actorMapper.entityToDto(expectedActor);
        assertEquals(expectedActorDto, actorDto);
    }

    @Test
    void dtoToEntityTest() {
        when(actorMapper.dtoToEntity(expectedActorDto)).thenReturn(expectedActor);
        Actor actor = actorMapper.dtoToEntity(expectedActorDto);
        assertEquals(expectedActor, actor);
    }
}

