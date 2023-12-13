package com.fatykhov.restapp.service.impl;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.impl.ClientRepositoryImpl;
import com.fatykhov.restapp.service.ClientService;

import java.util.List;

public class ClientServiceImpl implements ClientService {
    private final ClientRepositoryImpl clientRepositoryImpl;
    private final Mapper<Client, ClientDto> clientMapper;

    public ClientServiceImpl() {
        this.clientRepositoryImpl = new ClientRepositoryImpl();
        this.clientMapper = new ClientMapper();
    }

    public ClientServiceImpl(ClientRepositoryImpl clientRepositoryImpl, Mapper<Client, ClientDto> clientMapper) {
        this.clientRepositoryImpl = clientRepositoryImpl;
        this.clientMapper = clientMapper;
    }

    @Override
    public List<ClientDto> getAll() {
        List<Client> clientList = clientRepositoryImpl.findAll();
        return clientList.stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto getById(long id) {
        return clientMapper.toDto(clientRepositoryImpl.findOne(id));
    }

    public ClientDto save(ClientDto clientDto) {
        Client client = clientRepositoryImpl.save(clientMapper.fromDto(clientDto));
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDto update(long id, ClientDto updatedClientDto) {
        Client client = clientRepositoryImpl.update(id, clientMapper.fromDto(updatedClientDto));
        return clientMapper.toDto(client);
    }

    @Override
    public boolean remove(long id) {
        return clientRepositoryImpl.remove(id);
    }
}
