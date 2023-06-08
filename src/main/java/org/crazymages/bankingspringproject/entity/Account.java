package org.crazymages.bankingspringproject.entity;


import jakarta.persistence.*;
import lombok.Data;
import org.crazymages.bankingspringproject.entity.enums.AccountStatus;
import org.crazymages.bankingspringproject.entity.enums.AccountType;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "client_uuid")
    private UUID clientUuid;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "type")
    private AccountType type;

    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "currency_code")
    private CurrencyCode currencyCode;

}
