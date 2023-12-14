package com.fatykhov.restapi.mapper;

import com.fatykhov.restapi.util.TestObjectInitializer;
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
        expectedClientDto = TestObjectInitializer.initializeClientDto();
        expectedClient = TestObjectInitializer.initializeClient();
    }

    @Test
    void toDtoTest() {
        when(clientMapper.toDto(expectedClient)).thenReturn(expectedClientDto);
        ClientDto clientDto = clientMapper.toDto(expectedClient);
        assertEquals(expectedClientDto, clientDto);
    }

    @Test
    void fromDtoTest() {
        when(clientMapper.fromDto(expectedClientDto)).thenReturn(expectedClient);
        Client client = clientMapper.fromDto(expectedClientDto);
        assertEquals(expectedClient, client);
    }
}
