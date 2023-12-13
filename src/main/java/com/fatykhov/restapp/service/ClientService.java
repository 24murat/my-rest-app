package com.fatykhov.restapp.service;

import com.fatykhov.restapp.dto.ClientDto;
import com.fatykhov.restapp.entity.Client;
import com.fatykhov.restapp.mapper.ClientMapper;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.repository.ClientRepository;

import java.util.List;

public class ClientService {
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

    public List<ClientDto> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream()
                .map(clientMapper::toDto)
                .toList();
    }

    public ClientDto getClientById(long id) {
        return clientMapper.toDto(clientRepository.findOne(id));
    }

    public ClientDto saveClient(ClientDto clientDto) {
        Client client = clientRepository.save(clientMapper.fromDto(clientDto));
        return clientMapper.toDto(client);
    }

    public ClientDto updateClient(long id, ClientDto updatedClientDto) {
        Client client = clientRepository.update(id, clientMapper.fromDto(updatedClientDto));
        return clientMapper.toDto(client);
    }

    public boolean removeClient(long id) {
        return clientRepository.remove(id);
    }
}
