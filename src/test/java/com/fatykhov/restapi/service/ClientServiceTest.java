package com.fatykhov.restapi.service;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import com.fatykhov.restapp.repository.ClientRepository;
import com.fatykhov.restapp.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientService clientService;

    private ClientDto expectedClientDto;
    private Client expectedClient;

    @BeforeEach
    void setUp() {
        expectedClientDto = ClientDto.builder()
                .id(1)
                .name("TestClient")
                .build();

        expectedClient = Client.builder()
                .id(1)
                .name("TestClient")
                .build();
    }

    @Test
    void getAllClientsTest() {
        when(clientRepository.findAll()).thenReturn(List.of(expectedClient));
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);

        List<ClientDto> clients = clientService.getAllClients();

        assertEquals(List.of(expectedClientDto), clients);
    }

    @Test
    void getClientByIdTest() {
        int clientId = 1;
        when(clientRepository.findOne(clientId)).thenReturn(expectedClient);
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);

        ClientDto client = clientService.getClientById(clientId);

        assertEquals(expectedClientDto, client);
    }

    @Test
    void saveClientTest() {
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.save(expectedClient)).thenReturn(expectedClient);
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);

        ClientDto savedClient = clientService.saveClient(expectedClientDto);

        assertEquals(expectedClientDto, savedClient);
    }

    @Test
    void updateClientTest() {
        int clientId = 1;
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        when(clientRepository.update(clientId, expectedClient)).thenReturn(expectedClient);
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);

        ClientDto updatedClient = clientService.updateClient(clientId, expectedClientDto);

        assertEquals(expectedClientDto, updatedClient);
    }

    @Test
    void removeClientTest() {
        int clientId = 1;
        when(clientRepository.remove(clientId)).thenReturn(true);

        boolean result = clientService.removeClient(clientId);

        assertTrue(result);
    }
}
