package org.crazymages.bankingspringproject.dto.account;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountCreationDto {
    private String name;
    private String type;
    private String currencyCode;
}
