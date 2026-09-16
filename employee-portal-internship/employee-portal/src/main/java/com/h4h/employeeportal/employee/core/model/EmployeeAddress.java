package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "core_employee_address")
public class EmployeeAddress extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String municipality;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false, insertable = false, updatable = false)
    private EmployeeAddressStatusEnum addressStatus;
}
