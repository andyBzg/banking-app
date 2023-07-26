package org.crazymages.bankingspringproject.dto.mapper.client;

import org.crazymages.bankingspringproject.dto.ClientDto;
import org.crazymages.bankingspringproject.entity.Client;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.dto.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class ClientUpdateDtoMapper implements DtoMapper<Client, ClientDto> {
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

    @Override
    public List<ClientDto> getDtoList(List<Client> entityList) {
        return Optional.ofNullable(entityList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
