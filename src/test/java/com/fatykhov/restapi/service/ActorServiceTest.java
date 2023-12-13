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
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        List<ActorDto> actors = actorService.getAllActors();

        verify(actorMapper).toDto(expectedActor);
        assertEquals(List.of(expectedActorDto), actors);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getActorByIdTest(long id) {
        when(actorRepository.findOne(id)).thenReturn(expectedActor);
        when(spyService.getActorById(id)).thenReturn(expectedActorDto);

        ActorDto actorDto = spyService.getActorById(id);

        verify(actorMapper).toDto(expectedActor);
        assertEquals(expectedActorDto, actorDto);
    }

    @Test
    void saveActorTest() {
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.save(expectedActor)).thenReturn(expectedActor);
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        ActorDto savedActorDto = actorService.saveActor(expectedActorDto);

        verify(actorRepository).save(expectedActor);
        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        assertEquals(expectedActorDto, savedActorDto);
    }

    @Test
    void updateActorTest() {
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.update(eq(1L), eq(expectedActor))).thenReturn(expectedActor);

        ActorDto result = actorService.updateActor(1L, expectedActorDto);

        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        verify(actorRepository).update(eq(1L), eq(expectedActor));
        assertEquals(expectedActorDto, result);
    }

    @Test
    void removeActorTest() {
        when(actorRepository.remove(1)).thenReturn(true);

        boolean result = actorService.removeActor(1);

        assertTrue(result);
    }
}
