package org.crazymages.bankingspringproject.dto.manager;

import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object (DTO) class representing a Manager.
 */
@Data
@Builder
public class ManagerDto {
    private String firstName;
    private String lastName;
    private String status;
    private String description;
}
