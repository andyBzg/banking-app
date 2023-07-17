package org.crazymages.bankingspringproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object (DTO) class representing a Client.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    private String uuid;
    private String managerUuid;
    private String status;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
