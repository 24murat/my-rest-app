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
                .id(1L)
                .name("TestActor")
                .build();

        expectedActor = Actor.builder()
                .id(1L)
                .name("TestActor")
                .build();
    }

    @Test
    void getAllTest() {
        when(actorRepository.findAll()).thenReturn(List.of(expectedActor));
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        List<ActorDto> actors = actorService.getAll();

        verify(actorMapper).toDto(expectedActor);
        assertEquals(List.of(expectedActorDto), actors);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(actorRepository.findOne(id)).thenReturn(expectedActor);
        when(spyService.getById(id)).thenReturn(expectedActorDto);

        ActorDto actorDto = spyService.getById(id);

        verify(actorMapper).toDto(expectedActor);
        assertEquals(expectedActorDto, actorDto);
    }

    @Test
    void saveTest() {
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.save(expectedActor)).thenReturn(expectedActor);
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        ActorDto savedActorDto = actorService.save(expectedActorDto);

        verify(actorRepository).save(expectedActor);
        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        assertEquals(expectedActorDto, savedActorDto);
    }

    @Test
    void updateTest() {
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepository.update(eq(1L), eq(expectedActor))).thenReturn(expectedActor);

        ActorDto result = actorService.update(1L, expectedActorDto);

        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        verify(actorRepository).update(eq(1L), eq(expectedActor));
        assertEquals(expectedActorDto, result);
    }

    @Test
    void removeTest() {
        when(actorRepository.remove(1L)).thenReturn(true);

        boolean result = actorService.remove(1L);

        assertTrue(result);
    }
}
