package org.crazymages.bankingspringproject.service.utils.mapper;

import org.crazymages.bankingspringproject.dto.ManagerDTO;
import org.crazymages.bankingspringproject.entity.Manager;
import org.crazymages.bankingspringproject.exception.DataNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Component class that provides mapping functionality between Manager and ManagerDTO objects.
 */
@Component
public class ManagerDTOMapper {

    /**
     * Maps a Manager object to a ManagerDTO object.
     *
     * @param manager The Manager object to be mapped.
     * @return The mapped ManagerDTO object.
     */
    public ManagerDTO mapToManagerDTO(Manager manager) {
        return new ManagerDTO(
                manager.getUuid(),
                manager.getFirstName(),
                manager.getLastName(),
                manager.getStatus(),
                manager.getDescription()
        );
    }

    /**
     * Maps an ManagerDTO object to a Manager object.
     *
     * @param managerDTO The ManagerDTO object to be mapped.
     * @return The mapped Manager object.
     */
    public Manager mapToManager(ManagerDTO managerDTO) {
        Manager manager = new Manager();
        manager.setUuid(managerDTO.getUuid());
        manager.setFirstName(managerDTO.getFirstName());
        manager.setLastName(managerDTO.getLastName());
        manager.setStatus(managerDTO.getStatus());
        manager.setDescription(manager.getDescription());
        return manager;
    }

    /**
     * Maps a list of Manager objects to a list of ManagerDTO objects.
     *
     * @param managerList The list of Manager objects to be mapped.
     * @return The list of mapped ManagerDTO objects.
     * @throws DataNotFoundException If the input managerList is null.
     */
    public List<ManagerDTO> getListOfManagerDTOs(List<Manager> managerList) {
        return Optional.ofNullable(managerList)
                .orElseThrow(() -> new DataNotFoundException("list is null"))
                .stream()
                .filter(Objects::nonNull)
                .map(this::mapToManagerDTO)
                .toList();
    }
}
