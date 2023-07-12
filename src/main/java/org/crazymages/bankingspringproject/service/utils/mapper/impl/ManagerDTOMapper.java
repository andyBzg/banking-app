package org.crazymages.bankingspringproject.service.utils.mapper.impl;

import org.crazymages.bankingspringproject.dto.ManagerDTO;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.crazymages.bankingspringproject.service.utils.mapper.DTOMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Manager and ManagerDTO objects.
 */
@Component
public class ManagerDTOMapper implements DTOMapper<Manager, ManagerDTO> {

    @Override
    public ManagerDTO mapEntityToDto(Manager manager) {
        return new ManagerDTO(
                manager.getUuid(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getStatus(),
                manager.getDescription()
        );
    }

    @Override
    public Manager mapDtoToEntity(ManagerDTO managerDTO) {
        Manager manager = new Manager();
        manager.setUuid(managerDTO.getUuid());
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setStatus(managerDTO.getStatus());
        manager.setDescription(manager.getDescription());
        return manager;
    }

    @Override
    public List<ManagerDTO> getListOfDTOs(List<Manager> managerList) {
        return Optional.ofNullable(managerList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapEntityToDto)
                .toList();
    }
}
