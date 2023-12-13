package com.fatykhov.restapp.mapper;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;

public class ClientMapper implements Mapper<Client, ClientDto> {

    @Override
    public ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .name(client.getName())
                .build();
    }

    @Override
    public Client fromDto(ClientDto dto) {
        return Client.builder()
                .id(dto.getId())
                .name(dto.getName())
                .build();
    }
}
