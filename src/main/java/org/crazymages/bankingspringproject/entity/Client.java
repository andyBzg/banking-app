package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.crazymages.bankingspringproject.entity.enums.ClientStatus;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "manager_uuid")
    private UUID managerUuid;

    @Column(name = "status")
    private ClientStatus status;

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

}
