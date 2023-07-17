package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ClientDtoMapperTest {

    ClientDtoMapper clientDtoMapper;
    ClientDto clientDto;
    Client client1;
    Client client2;

    @BeforeEach
    void setUp() {
        clientDtoMapper = new ClientDtoMapper();
        clientDto = new ClientDto();

        client1 = new Client();
        client1.setUuid(UUID.randomUUID());
        client1.setManagerUuid(UUID.randomUUID());
        client1.setStatus(ClientStatus.ACTIVE);
        client1.setTaxCode("1234567890");
        client1.setFirstName("John");
        client1.setLastName("Doe");
        client1.setEmail("john.doe@example.com");
        client1.setAddress("123 Main St");
        client1.setPhone("555-1234");

        client2 = new Client();
        client2.setUuid(UUID.randomUUID());
        client2.setManagerUuid(UUID.randomUUID());
        client2.setStatus(ClientStatus.BLOCKED);
        client2.setTaxCode("0987654321");
        client2.setFirstName("Jane");
        client2.setLastName("Smith");
        client2.setEmail("jane.smith@example.com");
        client2.setAddress("456 Oak St");
        client2.setPhone("555-5678");
    }

    @Test
    void mapEntityToDto_validClient_success() {
        // when
        ClientDto clientDto = clientDtoMapper.mapEntityToDto(client1);

        // then
        assertEquals(client1.getUuid().toString(), clientDto.getUuid());
        assertEquals(client1.getManagerUuid().toString(), clientDto.getManagerUuid());
        assertEquals(client1.getStatus().toString(), clientDto.getStatus());
        assertEquals(client1.getTaxCode(), clientDto.getTaxCode());
        assertEquals(client1.getFirstName(), clientDto.getFirstName());
        assertEquals(client1.getLastName(), clientDto.getLastName());
        assertEquals(client1.getEmail(), clientDto.getEmail());
        assertEquals(client1.getAddress(), clientDto.getAddress());
        assertEquals(client1.getPhone(), clientDto.getPhone());
    }

    @Test
    void mapEntityToDto_nullClient_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> clientDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validClientDto_mappedSuccessfully() {
        // given
        clientDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        clientDto.setManagerUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        clientDto.setStatus("ACTIVE");
        clientDto.setTaxCode("1234567890");
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setEmail("john.doe@example.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhone("555-1234");

        // when
        Client client = clientDtoMapper.mapDtoToEntity(clientDto);

        // then
        assertEquals(UUID.fromString(clientDto.getUuid()), client.getUuid());
        assertEquals(UUID.fromString(clientDto.getManagerUuid()), client.getManagerUuid());
        assertEquals(ClientStatus.valueOf(clientDto.getStatus()), client.getStatus());
        assertEquals(clientDto.getTaxCode(), client.getTaxCode());
        assertEquals(clientDto.getFirstName(), client.getFirstName());
        assertEquals(clientDto.getLastName(), client.getLastName());
        assertEquals(clientDto.getEmail(), client.getEmail());
        assertEquals(clientDto.getAddress(), client.getAddress());
        assertEquals(clientDto.getPhone(), client.getPhone());
    }

    @Test
    void mapDtoToEntity_nullClientDto_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> clientDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingClientDtoProperties_throwsIllegalArgumentException() {
        // given
        clientDto.setUuid("30348dce-45f7-4e19-aa08-3ed77a8f7ac3");
        clientDto.setManagerUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");

        // when, then
        assertThrows(NullPointerException.class, () -> clientDtoMapper.mapDtoToEntity(clientDto));
    }

    @Test
    void getDtoList_validClientList_mappedSuccessfully() {
        // given
        List<Client> clientList = List.of(client1, client2);

        // when
        List<ClientDto> actual = clientDtoMapper.getDtoList(clientList);

        // then
        assertEquals(2, actual.size());

        ClientDto clientDto1 = actual.get(0);
        assertEquals(client1.getUuid().toString(), clientDto1.getUuid());
        assertEquals(client1.getManagerUuid().toString(), clientDto1.getManagerUuid());
        assertEquals(client1.getStatus().toString(), clientDto1.getStatus());
        assertEquals(client1.getTaxCode(), clientDto1.getTaxCode());
        assertEquals(client1.getFirstName(), clientDto1.getFirstName());
        assertEquals(client1.getLastName(), clientDto1.getLastName());
        assertEquals(client1.getEmail(), clientDto1.getEmail());
        assertEquals(client1.getAddress(), clientDto1.getAddress());
        assertEquals(client1.getPhone(), clientDto1.getPhone());

        ClientDto clientDto2 = actual.get(1);
        assertEquals(client2.getUuid().toString(), clientDto2.getUuid());
        assertEquals(client2.getManagerUuid().toString(), clientDto2.getManagerUuid());
        assertEquals(client2.getStatus().toString(), clientDto2.getStatus());
        assertEquals(client2.getTaxCode(), clientDto2.getTaxCode());
        assertEquals(client2.getFirstName(), clientDto2.getFirstName());
        assertEquals(client2.getLastName(), clientDto2.getLastName());
        assertEquals(client2.getEmail(), clientDto2.getEmail());
        assertEquals(client2.getAddress(), clientDto2.getAddress());
        assertEquals(client2.getPhone(), clientDto2.getPhone());
    }

    @Test
    void getDtoList_nullClientList_throwsDataNotFoundException() {
        assertThrows(DataNotFoundException.class, () -> clientDtoMapper.getDtoList(null));
    }

    @Test
    void getDtoList_emptyClientList_returnsEmptyList() {
        // given
        List<Client> clientList = Collections.emptyList();

        // when
        List<ClientDto> actual = clientDtoMapper.getDtoList(clientList);

        // then
        assertTrue(actual.isEmpty());
    }
}
