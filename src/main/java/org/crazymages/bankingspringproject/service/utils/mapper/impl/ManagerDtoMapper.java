package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ManagerDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DtoMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Manager and ManagerDTO objects.
 */
@Component
public class ManagerDtoMapper implements DtoMapper<Manager, ManagerDto> {

    @Override
    public ManagerDto mapEntityToDto(Manager manager) {
        if (manager == null) {
            throw new IllegalArgumentException("client cannot be null");
        }
        return ManagerDto.builder()
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .status(String.valueOf(manager.getStatus()))
                .description(manager.getDescription())
                .build();
    }

    @Override
    public Manager mapDtoToEntity(ManagerDto managerDto) {
        if (managerDto == null) {
            throw new IllegalArgumentException("clientDto cannot be null");
        }
        return Manager.builder()
                .firstName(managerDto.getFirstName())
                .lastName(managerDto.getLastName())
                .status(ManagerStatus.valueOf(managerDto.getStatus()))
                .description(managerDto.getDescription())
                .build();
    }

    @Override
    public List<ManagerDto> getDtoList(List<Manager> managerList) {
        return Optional.ofNullable(managerList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
