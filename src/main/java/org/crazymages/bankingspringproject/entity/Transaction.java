package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.crazymages.bankingspringproject.entity.enums.CurrencyCode;
import org.crazymages.bankingspringproject.entity.enums.TransactionType;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

/**
 * A class representing a transaction.
 * It stores information about a transaction entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "debit_account_uuid")
    private UUID debitAccountUuid;

    @Column(name = "credit_account_uuid")
    private UUID creditAccountUuid;

    @Column(name = "type")
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_code")
    private CurrencyCode currencyCode;

    @Column(name = "amount", precision = 12, scale = 2, columnDefinition = "default 0.00")
    private BigDecimal amount;

    @Column(name = "description")
    private String description;
}
