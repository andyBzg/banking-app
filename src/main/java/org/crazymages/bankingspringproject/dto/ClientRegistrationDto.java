package org.crazymages.bankingspringproject.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRegistrationDto {
    private String firstName;
    private String lastName;
    private String taxCode;
    private String address;
    private String phone;
    private String email;
    private String password;
}
