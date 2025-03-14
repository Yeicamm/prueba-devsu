package com.devsu.hackerearth.backend.client.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.devsu.hackerearth.backend.client.config.ClientNotFound;
import com.devsu.hackerearth.backend.client.model.Client;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.repository.ClientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

	private final ClientRepository clientRepository;
    private static final String CLIENT_NOT_FOUND_MESSAGE = "Cliente no encontrado.";

	@Override
	public List<ClientDto> getAll() {
		List<Client> clients = clientRepository.findAll();
		List<ClientDto> clientsDtos = new ArrayList<>();
		for (Client client : clients) {
			ClientDto dto = new ClientDto();
			BeanUtils.copyProperties(client, dto);
			clientsDtos.add(dto);
		}
		return clientsDtos;
	}

	@Override
	public ClientDto getById(Long id) {
		Optional<Client> client = clientRepository.findById(id);
		if(client.isEmpty()){
			throw new ClientNotFound(CLIENT_NOT_FOUND_MESSAGE);
		}
		ClientDto clientDto = new ClientDto();
		BeanUtils.copyProperties(client.get(), clientDto);
		return clientDto;
	}

	@Override
	@Transactional
	public ClientDto create(ClientDto clientDto) {
		Client client = new Client();
		BeanUtils.copyProperties(clientDto, client);
		Client savedClient = clientRepository.save(client);
		ClientDto savedClientDto = new ClientDto();
        BeanUtils.copyProperties(savedClient, savedClientDto);
		return savedClientDto;
	}

	@Override
	@Transactional
	public ClientDto update(ClientDto clientDto) {
		Optional<Client> client = clientRepository.findById(clientDto.getId());
		if(client.isEmpty()){
			throw new ClientNotFound(CLIENT_NOT_FOUND_MESSAGE);
		}
		Client existingClient = client.get();
		BeanUtils.copyProperties(clientDto, existingClient, "id");
		Client updatedClient = clientRepository.save(existingClient);
		ClientDto updatedClientDto = new ClientDto();
		BeanUtils.copyProperties(updatedClient, updatedClientDto);
		return updatedClientDto;
	}

	@Override
	@Transactional
	public ClientDto partialUpdate(Long id, PartialClientDto partialClientDto) {
    	Optional<Client> existingClientOpt = clientRepository.findById(id);
    
    	if (existingClientOpt.isPresent()) {
        	Client existingClient = existingClientOpt.get();

			if (partialClientDto != null) {
				existingClient.setStatus(partialClientDto.isStatus());
			}

			Client updatedClient = clientRepository.save(existingClient);
			
			ClientDto updatedClientDto = new ClientDto();
			BeanUtils.copyProperties(updatedClient, updatedClientDto);
			
			return updatedClientDto;
    	}
		throw new ClientNotFound(CLIENT_NOT_FOUND_MESSAGE);    
	}

	@Override
    @Transactional
    public void deleteById(Long id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
        } else {
			throw new ClientNotFound(CLIENT_NOT_FOUND_MESSAGE);
        }
    }
}
