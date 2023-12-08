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
                .id(1)
                .name("TestClient")
                .build();

        expectedClient = Client.builder()
                .id(1)
                .name("TestClient")
                .build();
    }

    @Test
    void entityToDtoTest() {
        // Устанавливаем поведение мока
        when(clientMapper.entityToDto(expectedClient)).thenReturn(expectedClientDto);
        // Вызываем метод маппинга
        ClientDto clientDto = clientMapper.entityToDto(expectedClient);
        // Проверяем, что маппер корректно преобразовал сущность в DTO
        assertEquals(expectedClientDto, clientDto);
    }

    @Test
    void dtoToEntityTest() {
        when(clientMapper.dtoToEntity(expectedClientDto)).thenReturn(expectedClient);
        // Вызываем метод маппинга
        Client client = clientMapper.dtoToEntity(expectedClientDto);
        // Проверяем, что маппер корректно преобразовал DTO в сущность
        assertEquals(expectedClient, client);
    }
}
