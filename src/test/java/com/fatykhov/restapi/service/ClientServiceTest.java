package com.fatykhov.restapi.service;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import com.fatykhov.restapp.repository.ClientRepository;
import com.fatykhov.restapp.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientMapper clientMapper;
    @InjectMocks
    private ClientService clientService;
    @Spy
    private ClientService spyService;

    private ClientDto expectedClientDto;
    private Client expectedClient;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository, clientMapper);
        spyService = spy(clientService);

        expectedClientDto = ClientDto.builder()
                .id(1)
                .name("TestClient")
                .build();

        expectedClient = Client.builder()
                .id(1)
                .name("TestClient")
                .build();
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(clientRepository.findOne(id)).thenReturn(expectedClient);
        when(spyService.getById(id)).thenReturn(expectedClientDto);

        ClientDto clientDto = spyService.getById(id);

        verify(clientMapper).toDto(expectedClient);
        assertEquals(expectedClientDto, clientDto);
    }

    @Test
    void saveTest() {
        when(clientMapper.fromDto(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.save(expectedClient)).thenReturn(expectedClient);
        when(clientMapper.toDto(expectedClient)).thenReturn(expectedClientDto);

        ClientDto savedClientDto = clientService.save(expectedClientDto);

        verify(clientRepository).save(expectedClient);
        verify(clientMapper).toDto(expectedClient);
        verify(clientMapper).fromDto(expectedClientDto);
        assertEquals(expectedClientDto, savedClientDto);
    }

    @Test
    void updateTest() {
        when(clientMapper.toDto(expectedClient)).thenReturn(expectedClientDto);
        when(clientMapper.fromDto(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.update(eq(1L), eq(expectedClient))).thenReturn(expectedClient);

        ClientDto result = clientService.update(1L, expectedClientDto);

        verify(clientMapper).toDto(expectedClient);
        verify(clientMapper).fromDto(expectedClientDto);
        verify(clientRepository).update(eq(1L), eq(expectedClient));
        assertEquals(expectedClientDto, result);
    }

    @Test
    void removeTest() {
        when(clientRepository.remove(1L)).thenReturn(true);

        boolean result = clientService.remove(1L);

        assertTrue(result);
    }
}
