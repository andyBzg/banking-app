package org.crazymages.bankingspringproject.dto.manager;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ManagerRegistrationDto {
    private String firstName;
    private String lastName;
    private String description;
    private String login;
    private String password;
}
