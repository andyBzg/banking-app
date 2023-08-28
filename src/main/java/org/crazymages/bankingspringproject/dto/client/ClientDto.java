package org.crazymages.bankingspringproject.dto.client;

import lombok.Builder;
import lombok.Data;

/**
 * Data transfer object (DTO) class representing a Client.
 */
@Data
@Builder
public class ClientDto {
    private String managerUuid;
    private String status;
    private String taxCode;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phone;
}
