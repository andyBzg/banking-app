package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;

import java.util.UUID;

/**
 * Data transfer object (DTO) class representing a Manager.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDTO {
    private UUID uuid;
    private String firstName;
    private String lastName;
    private ManagerStatus status;
    private String description;
}
