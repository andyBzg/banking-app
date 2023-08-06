package org.crazymages.bankingspringproject.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    private String username;
    private String password;
    private boolean enabled;
}
