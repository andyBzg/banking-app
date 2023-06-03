package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "agreements")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "interest_rate", precision = 6, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "status")
    private Integer status;

    @Column(name = "sum", precision = 15, scale = 2)
    private BigDecimal sum;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
