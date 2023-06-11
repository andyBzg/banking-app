package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.crazymages.bankingspringproject.entity.enums.AgreementStatus;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "agreements")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    @Column(name = "account_uuid")
    private UUID accountUuid;

    @Column(name = "product_uuid")
    private UUID productUuid;

    @Column(name = "interest_rate", precision = 6, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "status")
    private AgreementStatus status;

    @Column(name = "sum", precision = 15, scale = 2)
    private BigDecimal sum;

}
