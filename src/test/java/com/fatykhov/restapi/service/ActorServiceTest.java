package com.fatykhov.restapi.service;

import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import com.fatykhov.restapp.repository.ActorRepository;
import com.fatykhov.restapp.service.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    @Mock
    private ActorRepository actorRepository;
    @Mock
    private ActorMapper actorMapper;
    @InjectMocks
    private ActorService actorService;
    @Spy
    private ActorService spyService;

    private ActorDto expectedActorDto;
    private Actor expectedActor;

    @BeforeEach
    void setUp() {
        actorService = new ActorService(actorRepository, actorMapper);
        spyService = spy(actorService);

        expectedActorDto = ActorDto.builder()
                .id(1)
                .name("TestActor")
                .build();

        expectedActor = Actor.builder()
                .id(1)
                .name("TestActor")
                .build();
    }

    @Test
    void getAllActorsTest() {
        when(actorRepository.findAll()).thenReturn(List.of(expectedActor));
        when(actorMapper.entityToDto(expectedActor)).thenReturn(expectedActorDto);

        List<ActorDto> actors = actorService.getAllActors();

        verify(actorMapper).entityToDto(expectedActor);
        assertEquals(List.of(expectedActorDto), actors);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void getActorByIdTest(int id) {
        when(actorRepository.findOne(id)).thenReturn(expectedActor);
        when(spyService.getActorById(id)).thenReturn(expectedActorDto);

        ActorDto actorDto = spyService.getActorById(id);

        verify(actorMapper).entityToDto(expectedActor);
        assertEquals(expectedActorDto, actorDto);
    }

    @Test
    void saveActorTest() {
        when(actorMapper.dtoToEntity(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.save(expectedActor)).thenReturn(expectedActor);
        when(actorMapper.entityToDto(expectedActor)).thenReturn(expectedActorDto);

        ActorDto savedActorDto = actorService.saveActor(expectedActorDto);

        verify(actorRepository).save(expectedActor);
        verify(actorMapper).entityToDto(expectedActor);
        verify(actorMapper).dtoToEntity(expectedActorDto);
        assertEquals(expectedActorDto, savedActorDto);
    }

    @Test
    void updateActorTest() {
        when(actorMapper.entityToDto(expectedActor)).thenReturn(expectedActorDto);
        when(actorMapper.dtoToEntity(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.update(eq(1), eq(expectedActor))).thenReturn(expectedActor);

        ActorDto result = actorService.updateActor(1, expectedActorDto);

        verify(actorMapper).entityToDto(expectedActor);
        verify(actorMapper).dtoToEntity(expectedActorDto);
        verify(actorRepository).update(eq(1), eq(expectedActor));
        assertEquals(expectedActorDto, result);
    }

    @Test
    void removeActorTest() {
        when(actorRepository.remove(1)).thenReturn(true);

        boolean result = actorService.removeActor(1);

        assertTrue(result);
    }
}
