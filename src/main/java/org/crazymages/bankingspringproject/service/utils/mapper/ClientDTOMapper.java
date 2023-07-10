package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.dto.ClientDTO;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Client and ClientDTO objects.
 */
@Component
public class ClientDTOMapper {

    /**
     * Maps a Client object to an ClientDTO object.
     *
     * @param client The Client object to be mapped.
     * @return The mapped ClientDTO object.
     */
    public ClientDTO mapToClientDTO(Client client) {
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

    /**
     * Maps an ClientDTO object to a Client object.
     *
     * @param clientDTO The ClientDTO object to be mapped.
     * @return The mapped Client object.
     */
    public Client mapToClient(ClientDTO clientDTO) {
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

    /**
     * Maps a list of Client objects to a list of ClientDTO objects.
     *
     * @param clientList The list of Client objects to be mapped.
     * @return The list of mapped ClientDTO objects.
     * @throws DataNotFoundException If the input clientList is null.
     */
    public List<ClientDTO> getListOfAgreementDTOs(List<Client> clientList) {
        return Optional.ofNullable(clientList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToClientDTO)
                .toList();
    }
}
