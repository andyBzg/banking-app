package org.crazymages.bankingspringproject.entity.enums;

/**
 * Enum representing roles for access control in Spring Security.
 * These roles can be used to define access permissions for different parts of the application.
 */
public enum Role {

    /**
     * The role for administrators.
     * Administrators have the highest level of access and can perform administrative tasks.
     */
    ADMIN,

    /**
     * The role for managers.
     * Managers have access to specific management functionalities.
     */
    MANAGER,

    /**
     * The role for regular users.
     * Users have limited access and can perform basic operations.
     */
    USER
}
