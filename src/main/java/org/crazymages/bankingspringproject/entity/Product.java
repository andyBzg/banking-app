package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "name", length = 70)
    private String name;

    @Column(name = "currency_code")
    private Integer currencyCode;

    @Column(name = "interest_rate", precision = 6, scale = 4)
    private BigDecimal interestRate;

    @Column(name = "limit", precision = 15, scale = 2)
    private BigDecimal limit;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
