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
import java.util.UUID;

/**
 * Component class that provides mapping functionality between Manager and ManagerDTO objects.
 */
@Component
public class ManagerDtoMapper implements DtoMapper<Manager, ManagerDto> {

    @Override
    public ManagerDto mapEntityToDto(Manager manager) {
        return new ManagerDto(
                String.valueOf(manager.getUuid()),
                manager.getFirstName(),
                manager.getLastName(),
                String.valueOf(manager.getStatus()),
                manager.getDescription()
        );
    }

    @Override
    public Manager mapDtoToEntity(ManagerDto managerDto) {
        Manager manager = new Manager();
        manager.setUuid(UUID.fromString(managerDto.getUuid()));
        manager.setFirstName(managerDto.getFirstName());
        manager.setLastName(managerDto.getLastName());
        manager.setStatus(ManagerStatus.valueOf(managerDto.getStatus()));
        manager.setDescription(manager.getDescription());
        return manager;
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
