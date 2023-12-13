package com.fatykhov.restapp.service;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.ClientRepository;
import com.fatykhov.restapp.service.interfaces.ClientServiceInterface;

import java.util.List;

public class ClientService implements ClientServiceInterface {
    private final ClientRepository clientRepository;
    private final Mapper<Client, ClientDto> clientMapper;

    public ClientService() {
        this.clientRepository = new ClientRepository();
        this.clientMapper = new ClientMapper();
    }

    public ClientService(ClientRepository clientRepository, Mapper<Client, ClientDto> clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public List<ClientDto> getAll() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream()
                .map(clientMapper::toDto)
                .toList();
    }

    @Override
    public ClientDto getById(long id) {
        return clientMapper.toDto(clientRepository.findOne(id));
    }

    public ClientDto save(ClientDto clientDto) {
        Client client = clientRepository.save(clientMapper.fromDto(clientDto));
        return clientMapper.toDto(client);
    }

    @Override
    public ClientDto update(long id, ClientDto updatedClientDto) {
        Client client = clientRepository.update(id, clientMapper.fromDto(updatedClientDto));
        return clientMapper.toDto(client);
    }

    @Override
    public boolean remove(long id) {
        return clientRepository.remove(id);
    }
}
