package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "clients_table")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "tax_code", length = 20)
    private String taxCode;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "address", length = 80)
    private String address;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;
}
