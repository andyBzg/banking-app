package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.crazymages.bankingspringproject.entity.enums.ManagerStatus;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@Entity
@Table(name = "managers")
public class Manager {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid")
    private UUID uuid;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "status")
    private ManagerStatus status;

    @Column(name = "description")
    private String description;

}
