package org.crazymages.bankingspringproject.dto.manager.mapper;

import org.crazymages.bankingspringproject.dto.manager.ManagerDto;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.crazymages.bankingspringproject.dto.DtoMapper;
import org.springframework.stereotype.Component;

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
                .status(manager.getStatus() != null ? manager.getStatus().name() : null)
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
                .status(managerDto.getStatus() != null ? ManagerStatus.valueOf(managerDto.getStatus()) : null)
                .description(managerDto.getDescription())
                .build();
    }
}
