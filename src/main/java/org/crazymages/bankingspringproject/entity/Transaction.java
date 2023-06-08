package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "debit_account_uuid")
    private UUID debitAccountUuid;

    @Column(name = "credit_account_uuid")
    private UUID creditAccountUuid;

    @Column(name = "type")
    private TransactionType type;

    @Column(name = "currency_code")
    private CurrencyCode currencyCode;

    @Column(name = "amount", precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "description")
    private String description;

}
