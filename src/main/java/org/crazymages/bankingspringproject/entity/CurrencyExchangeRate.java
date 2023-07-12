package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;


@Data
@Entity
@Table(name = "currency_exchange_rate")
public class CurrencyExchangeRate {

    @Id
    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "exchange_rate")
    private BigDecimal exchangeRate;

    @Column(name = "is_deleted", columnDefinition = "boolean default false")
    private boolean isDeleted;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;
}
