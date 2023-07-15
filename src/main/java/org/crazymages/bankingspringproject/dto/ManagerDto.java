package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) class representing a Manager.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDto {
    private String uuid;
    private String firstName;
    private String lastName;
    private String status;
    private String description;
}
