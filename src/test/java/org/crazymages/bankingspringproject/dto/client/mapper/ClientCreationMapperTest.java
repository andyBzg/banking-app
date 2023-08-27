package org.crazymages.bankingspringproject.dto.client.mapper;

import org.crazymages.bankingspringproject.dto.client.ClientDto;
import org.crazymages.bankingspringproject.dto.client.mapper.ClientCreationMapper;
import org.crazymages.bankingspringproject.entity.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class ClientCreationMapperTest {

    ClientCreationMapper clientCreationMapper;
    ClientDto clientDto;
    Client client1;

    @BeforeEach
    void setUp() {
        clientCreationMapper = new ClientCreationMapper();

        clientDto = ClientDto.builder().build();

        client1 = Client.builder()
                .taxCode("1234567890")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("123 Main St")
                .phone("555-1234")
                .build();
    }

    @Test
    void mapEntityToDto_validClient_success() {
        // when
        ClientDto clientDto = clientCreationMapper.mapEntityToDto(client1);

        // then
        assertEquals(client1.getTaxCode(), clientDto.getTaxCode());
        assertEquals(client1.getFirstName(), clientDto.getFirstName());
        assertEquals(client1.getLastName(), clientDto.getLastName());
        assertEquals(client1.getEmail(), clientDto.getEmail());
        assertEquals(client1.getAddress(), clientDto.getAddress());
        assertEquals(client1.getPhone(), clientDto.getPhone());
    }

    @Test
    void mapEntityToDto_withNullClientProperties_returnsClientDtoWithNullProperties() {
        // given
        Client client = new Client();

        // when
        ClientDto clientDto = clientCreationMapper.mapEntityToDto(client);

        // then
        assertNull(clientDto.getTaxCode());
        assertNull(clientDto.getFirstName());
        assertNull(clientDto.getLastName());
        assertNull(clientDto.getEmail());
        assertNull(clientDto.getAddress());
        assertNull(clientDto.getPhone());
    }

    @Test
    void mapEntityToDto_nullClient_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> clientCreationMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validClientDto_success() {
        // given
        clientDto.setTaxCode("1234567890");
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setEmail("john.doe@example.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhone("555-1234");

        // when
        Client actual = clientCreationMapper.mapDtoToEntity(clientDto);

        // then
        assertFalse(actual.isDeleted());
        assertEquals(clientDto.getTaxCode(), actual.getTaxCode());
        assertEquals(clientDto.getFirstName(), actual.getFirstName());
        assertEquals(clientDto.getLastName(), actual.getLastName());
        assertEquals(clientDto.getEmail(), actual.getEmail());
        assertEquals(clientDto.getAddress(), actual.getAddress());
        assertEquals(clientDto.getPhone(), actual.getPhone());
    }

    @Test
    void mapDtoToEntity_nullClientDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> clientCreationMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingClientDtoProperties_returnsClientWithNullProperties() {
        // when
        Client actual = clientCreationMapper.mapDtoToEntity(clientDto);

        // then
        assertFalse(actual.isDeleted());
        assertNull(actual.getTaxCode());
        assertNull(actual.getFirstName());
        assertNull(actual.getLastName());
        assertNull(actual.getEmail());
        assertNull(actual.getAddress());
        assertNull(actual.getPhone());
    }
}
