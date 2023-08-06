package org.crazymages.bankingspringproject.dto.mapper.client;

import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        clientDto = ClientDto.builder().build();

        client1 = Client.builder()
                .uuid(UUID.fromString("f5dce10d-b822-47d7-ab96-919a93a812ec"))
                .managerUuid(UUID.fromString("1ab9c0a8-22d2-49d1-a22e-8029c1d11745"))
                .status(ClientStatus.ACTIVE)
                .taxCode("1234567890")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .address("123 Main St")
                .phone("555-1234")
                .build();

        client2 = Client.builder()
                .uuid(UUID.fromString("f59f83b7-9f9b-495b-83e7-09c11856e6a5"))
                .managerUuid(UUID.fromString("30348dce-45f7-4e19-aa08-3ed77a8f7ac3"))
                .status(ClientStatus.BLOCKED)
                .taxCode("0987654321")
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .address("456 Oak St")
                .phone("555-5678")
                .build();
    }

    @Test
    void mapEntityToDto_validClient_success() {
        // when
        ClientDto clientDto = clientDtoMapper.mapEntityToDto(client1);

        // then
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
    void mapEntityToDto_withNullClientProperties_returnsClientDtoWithNullProperties() {
        // given
        Client client = new Client();

        // when
        ClientDto clientDto = clientDtoMapper.mapEntityToDto(client);

        // then
        assertNull(clientDto.getManagerUuid());
        assertNull(clientDto.getStatus());
        assertNull(clientDto.getTaxCode());
        assertNull(clientDto.getFirstName());
        assertNull(clientDto.getLastName());
        assertNull(clientDto.getEmail());
        assertNull(clientDto.getAddress());
        assertNull(clientDto.getPhone());
    }

    @Test
    void mapEntityToDto_nullClient_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> clientDtoMapper.mapEntityToDto(null));
    }

    @Test
    void mapDtoToEntity_validClientDto_success() {
        // given
        clientDto.setManagerUuid("f59f83b7-9f9b-495b-83e7-09c11856e6a5");
        clientDto.setStatus("ACTIVE");
        clientDto.setTaxCode("1234567890");
        clientDto.setFirstName("John");
        clientDto.setLastName("Doe");
        clientDto.setEmail("john.doe@example.com");
        clientDto.setAddress("123 Main St");
        clientDto.setPhone("555-1234");

        // when
        Client actual = clientDtoMapper.mapDtoToEntity(clientDto);

        // then
        assertFalse(actual.isDeleted());
        assertEquals(UUID.fromString(clientDto.getManagerUuid()), actual.getManagerUuid());
        assertEquals(clientDto.getStatus(), actual.getStatus().name());
        assertEquals(clientDto.getTaxCode(), actual.getTaxCode());
        assertEquals(clientDto.getFirstName(), actual.getFirstName());
        assertEquals(clientDto.getLastName(), actual.getLastName());
        assertEquals(clientDto.getEmail(), actual.getEmail());
        assertEquals(clientDto.getAddress(), actual.getAddress());
        assertEquals(clientDto.getPhone(), actual.getPhone());
    }

    @Test
    void mapDtoToEntity_nullClientDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> clientDtoMapper.mapDtoToEntity(null));
    }

    @Test
    void mapDtoToEntity_missingClientDtoProperties_returnsClientWithNullProperties() {
        // when
        Client actual = clientDtoMapper.mapDtoToEntity(clientDto);

        // then
        assertFalse(actual.isDeleted());
        assertNull(actual.getManagerUuid());
        assertNull(actual.getStatus());
        assertNull(actual.getTaxCode());
        assertNull(actual.getFirstName());
        assertNull(actual.getLastName());
        assertNull(actual.getEmail());
        assertNull(actual.getAddress());
        assertNull(actual.getPhone());
    }
}
