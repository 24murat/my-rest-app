package com.fatykhov.restapi.service;

import com.fatykhov.restapi.util.TestObjectInitializer;
import com.fatykhov.restapp.dto.ActorDto;
import com.fatykhov.restapp.entity.Actor;
import com.fatykhov.restapp.mapper.ActorMapper;
import com.fatykhov.restapp.repository.impl.ActorRepositoryImpl;
import com.fatykhov.restapp.service.impl.ActorServiceImpl;
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
public class ActorServiceImplTest {

    @Mock
    private ActorRepositoryImpl actorRepositoryImpl;
    @Mock
    private ActorMapper actorMapper;
    @InjectMocks
    private ActorServiceImpl actorServiceImpl;
    @Spy
    private ActorServiceImpl spyService;

    private ActorDto expectedActorDto;
    private Actor expectedActor;

    @BeforeEach
    void setUp() {
        actorServiceImpl = new ActorServiceImpl(actorRepositoryImpl, actorMapper);
        spyService = spy(actorServiceImpl);

        expectedActorDto = TestObjectInitializer.initializeActorDto();
        expectedActor = TestObjectInitializer.initializeActor();
    }

    @Test
    void getAllTest() {
        when(actorRepositoryImpl.findAll()).thenReturn(List.of(expectedActor));
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        List<ActorDto> actors = actorServiceImpl.getAll();

        verify(actorMapper).toDto(expectedActor);
        assertEquals(List.of(expectedActorDto), actors);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(actorRepositoryImpl.findOne(id)).thenReturn(expectedActor);
        when(spyService.getById(id)).thenReturn(expectedActorDto);

        ActorDto actorDto = spyService.getById(id);

        verify(actorMapper).toDto(expectedActor);
        assertEquals(expectedActorDto, actorDto);
    }

    @Test
    void saveTest() {
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepositoryImpl.save(expectedActor)).thenReturn(expectedActor);
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);

        ActorDto savedActorDto = actorServiceImpl.save(expectedActorDto);

        verify(actorRepositoryImpl).save(expectedActor);
        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        assertEquals(expectedActorDto, savedActorDto);
    }

    @Test
    void updateTest() {
        when(actorMapper.toDto(expectedActor)).thenReturn(expectedActorDto);
        when(actorMapper.fromDto(expectedActorDto)).thenReturn(expectedActor);
        when(actorRepositoryImpl.update(eq(1L), eq(expectedActor))).thenReturn(expectedActor);

        ActorDto result = actorServiceImpl.update(1L, expectedActorDto);

        verify(actorMapper).toDto(expectedActor);
        verify(actorMapper).fromDto(expectedActorDto);
        verify(actorRepositoryImpl).update(eq(1L), eq(expectedActor));
        assertEquals(expectedActorDto, result);
    }

    @Test
    void removeTest() {
        when(actorRepositoryImpl.remove(1L)).thenReturn(true);

        boolean result = actorServiceImpl.remove(1L);

        assertTrue(result);
    }
}
