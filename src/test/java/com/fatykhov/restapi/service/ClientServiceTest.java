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
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8})
    void getClientByIdTest(int id) {
        when(clientRepository.findOne(id)).thenReturn(expectedClient);
        when(spyService.getClientById(id)).thenReturn(expectedClientDto);

        ClientDto clientDto = spyService.getClientById(id);

        verify(clientMapper).entityToDto(expectedClient);
        assertEquals(expectedClientDto, clientDto);
    }

    @Test
    void saveClientTest() {
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.save(expectedClient)).thenReturn(expectedClient);
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);

        ClientDto savedClientDto = clientService.saveClient(expectedClientDto);

        verify(clientRepository).save(expectedClient);
        verify(clientMapper).entityToDto(expectedClient);
        verify(clientMapper).dtoToEntity(expectedClientDto);
        assertEquals(expectedClientDto, savedClientDto);
    }

    @Test
    void updateClientTest() {
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.update(eq(1), eq(expectedClient))).thenReturn(expectedClient);

        ClientDto result = clientService.updateClient(1, expectedClientDto);

        verify(clientMapper).entityToDto(expectedClient);
        verify(clientMapper).dtoToEntity(expectedClientDto);
        verify(clientRepository).update(eq(1), eq(expectedClient));
        assertEquals(expectedClientDto, result);
    }

    @Test
    void removeClientTest() {
        when(clientRepository.remove(1)).thenReturn(true);

        boolean result = clientService.removeClient(1);

        assertTrue(result);
    }
}
