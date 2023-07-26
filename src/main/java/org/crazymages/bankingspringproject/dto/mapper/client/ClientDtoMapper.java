package org.crazymages.bankingspringproject.dto.mapper.client;

import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Component class that provides mapping functionality between Client and ClientDTO objects.
 */
@Component
public class ClientDtoMapper implements DtoMapper<Client, ClientDto> {

    @Override
    public ClientDto mapEntityToDto(Client client) {
        if (client == null) {
            throw new IllegalArgumentException("client cannot be null");
        }
        return ClientDto.builder()
                .managerUuid(client.getManagerUuid() != null ? client.getManagerUuid().toString() : null)
                .status(client.getStatus() != null ? client.getStatus().name() : null)
                .taxCode(client.getTaxCode())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .email(client.getEmail())
                .address(client.getAddress())
                .phone(client.getPhone())
                .build();
    }

    @Override
    public Client mapDtoToEntity(ClientDto clientDto) {
        if (clientDto == null) {
            throw new IllegalArgumentException("clientDto cannot be null");
        }
        return Client.builder()
                .managerUuid(clientDto.getManagerUuid() != null ? UUID.fromString(clientDto.getManagerUuid()) : null)
                .status(clientDto.getStatus() != null ? ClientStatus.valueOf(clientDto.getStatus()) : null)
                .taxCode(clientDto.getTaxCode())
                .firstName(clientDto.getFirstName())
                .lastName(clientDto.getLastName())
                .email(clientDto.getEmail())
                .address(clientDto.getAddress())
                .phone(clientDto.getPhone())
                .build();
    }
}
