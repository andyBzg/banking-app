package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;

import java.util.UUID;

/**
 * Data transfer object (DTO) class representing a Client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {
    private UUID uuid;
    private UUID managerUuid;
    private ClientStatus status;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
