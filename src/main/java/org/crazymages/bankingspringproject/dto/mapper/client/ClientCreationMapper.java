package org.crazymages.bankingspringproject.dto.mapper.client;

import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

@Component
public class ClientCreationMapper implements DtoMapper<Client, ClientDto> {
    @Override
    public ClientDto mapEntityToDto(Client entity) {
        if (entity == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return ClientDto.builder()
                .taxCode(entity.getTaxCode())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .phone(entity.getPhone())
                .build();
    }

    @Override
    public Client mapDtoToEntity(ClientDto entityDto) {
        if (entityDto == null) {
            throw new IllegalArgumentException("account cannot be null");
        }
        return Client.builder()
                .taxCode(entityDto.getTaxCode())
                .firstName(entityDto.getFirstName())
                .lastName(entityDto.getLastName())
                .email(entityDto.getEmail())
                .address(entityDto.getAddress())
                .phone(entityDto.getPhone())
                .build();
    }
}
