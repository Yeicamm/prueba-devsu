package com.devsu.hackerearth.backend.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.devsu.hackerearth.backend.client.controller.ClientController;
import com.devsu.hackerearth.backend.client.model.dto.ClientDto;
import com.devsu.hackerearth.backend.client.model.dto.PartialClientDto;
import com.devsu.hackerearth.backend.client.service.ClientService;

public class SampleTest {

    private ClientService clientService;
    private ClientController clientController;

    @BeforeEach
    void setUp() {
        clientService = mock(ClientService.class); 
        clientController = new ClientController(clientService);
    }

    @Test
    void createClientTest() {
        ClientDto newClientDto = new ClientDto(1L, "Dni", "Name", "Password", "Gender", 1, "Address", "9999999999", true);
        when(clientService.create(newClientDto)).thenReturn(newClientDto);

        ResponseEntity<ClientDto> response = clientController.create(newClientDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(newClientDto, response.getBody());
    }

    @Test
    void getAllClientsTest() {
        List<ClientDto> clients = Arrays.asList(
            new ClientDto(1L, "12345678", "John Doe", "password", "M", 30, "Address", "9999999999", true),
            new ClientDto(2L, "87654321", "Jane Doe", "password", "F", 28, "Address", "8888888888", false)
        );

        when(clientService.getAll()).thenReturn(clients);

        ResponseEntity<List<ClientDto>> response = clientController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getClientByIdTest() {
        ClientDto client = new ClientDto(1L, "12345678", "John Doe", "password", "M", 30, "Address", "9999999999", true);
        when(clientService.getById(1L)).thenReturn(client);

        ResponseEntity<ClientDto> response = clientController.get(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void updateClientTest() {
        ClientDto updatedClientDto = new ClientDto(1L, "12345678", "John Doe", "password", "M", 30, "New Address", "9999999999", true);
        when(clientService.update(updatedClientDto)).thenReturn(updatedClientDto);

        ResponseEntity<ClientDto> response = clientController.update(1L, updatedClientDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("New Address", response.getBody().getAddress());
    }

    @Test
    void partialUpdateClientTest() {
        PartialClientDto partialUpdate = new PartialClientDto(false);
        ClientDto updatedClient = new ClientDto(1L, "12345678", "John Doe", "password", "M", 30, "Address", "9999999999", false);

        when(clientService.partialUpdate(1L, partialUpdate)).thenReturn(updatedClient);

        ResponseEntity<ClientDto> response = clientController.partialUpdate(1L, partialUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
    }

    @Test
    void deleteClientTest() {
        doNothing().when(clientService).deleteById(1L);

        ResponseEntity<Void> response = clientController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).deleteById(1L);
    }

    @Test
    void getAllClientsControllerTest() {
        List<ClientDto> clients = Arrays.asList(
            new ClientDto(1L, "12345678", "Cristian 1", "password", "M", 30, "Address", "9999999999", true),
            new ClientDto(2L, "87654321", "Cristian 2", "password", "F", 28, "Address", "8888888888", false)
        );

        when(clientService.getAll()).thenReturn(clients);

        ResponseEntity<List<ClientDto>> response = clientController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getClientByIdControllerTest() {
        ClientDto client = new ClientDto(1L, "12345678", "Cristian 1", "password", "M", 30, "Address", "9999999999", true);
        when(clientService.getById(1L)).thenReturn(client);

        ResponseEntity<ClientDto> response = clientController.get(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(client, response.getBody());
    }

    @Test
    void partialUpdateClientControllerTest() {
        PartialClientDto partialUpdate = new PartialClientDto(false);
        ClientDto updatedClient = new ClientDto(1L, "12345678", "Cristian 1", "password", "M", 30, "Address", "9999999999", false);

        when(clientService.partialUpdate(1L, partialUpdate)).thenReturn(updatedClient);

        ResponseEntity<ClientDto> response = clientController.partialUpdate(1L, partialUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isStatus());
    }

    @Test
    void deleteClientControllerTest() {
        doNothing().when(clientService).deleteById(1L);

        ResponseEntity<Void> response = clientController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(clientService, times(1)).deleteById(1L);
    }
}
