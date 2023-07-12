package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ClientDTO;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Client and ClientDTO objects.
 */
@Component
public class ClientDTOMapper implements DTOMapper<Client, ClientDTO> {

    @Override
    public ClientDTO mapEntityToDto(Client client) {
        return new ClientDTO(
                client.getUuid(),
                client.getManagerUuid(),
                client.getStatus(),
                client.getTaxCode(),
                client.getFirstName(),
                client.getLastName(),
                client.getEmail(),
                client.getAddress(),
                client.getPhone()
        );
    }

    @Override
    public Client mapDtoToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setUuid(clientDTO.getUuid());
        client.setManagerUuid(clientDTO.getManagerUuid());
        client.setStatus(clientDTO.getStatus());
        client.setTaxCode(clientDTO.getTaxCode());
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setAddress(clientDTO.getAddress());
        client.setPhone(clientDTO.getPhone());
        return client;
    }

    @Override
    public List<ClientDTO> getListOfDTOs(List<Client> clientList) {
        return Optional.ofNullable(clientList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
