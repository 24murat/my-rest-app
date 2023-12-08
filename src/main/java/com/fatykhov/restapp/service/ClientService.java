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

    public List<ClientDto> getAllClients() {
        List<Client> clientList = clientRepository.findAll();
        return clientList.stream()
                .map(clientMapper::entityToDto)
                .toList();
    }

    public ClientDto getClientById(int id) {
        return clientMapper.entityToDto(clientRepository.findOne(id));
    }

    public ClientDto saveClient(ClientDto clientDto) {
        Client client = clientRepository.save(clientMapper.dtoToEntity(clientDto));
        return clientMapper.entityToDto(client);
    }

    public ClientDto updateClient(int id, ClientDto updatedClientDto) {
        Client client = clientRepository.update(id, clientMapper.dtoToEntity(updatedClientDto));
        return clientMapper.entityToDto(client);
    }

    public boolean removeClient(int id) {
        return clientRepository.remove(id);
    }

}
