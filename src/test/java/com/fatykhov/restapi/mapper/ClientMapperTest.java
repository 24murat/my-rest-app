package com.fatykhov.restapi.mapper;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientMapperTest {

    @Mock
    private ClientMapper clientMapper;

    private ClientDto expectedClientDto;
    private Client expectedClient;

    @BeforeEach
    void setUp() {
        expectedClientDto = ClientDto.builder()
                .id(1L)
                .name("TestClient")
                .build();

        expectedClient = Client.builder()
                .id(1L)
                .name("TestClient")
                .build();
    }

    @Test
    void entityToDtoTest() {
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);
        ClientDto clientDto = clientMapper.entityToDto(expectedClient);
        assertEquals(expectedClientDto, clientDto);
    }

    @Test
    void dtoToEntityTest() {
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        Client client = clientMapper.dtoToEntity(expectedClientDto);
        assertEquals(expectedClient, client);
    }
}
