package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserRoleEnum;
import com.h4h.employeeportal.employee.core.enumeration.UserStatusEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "core_user")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private UserRoleEnum role;

    @Column(nullable = false)
    private UserStatusEnum status;

    @Column(nullable = false)
    private LanguageEnum language;
}
