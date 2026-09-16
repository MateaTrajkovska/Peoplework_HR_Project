package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "core_employee_contact")
public class EmployeeContact extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    private EmployeeContactTypeEnum contactType;

    @Column(nullable = false, insertable = false, updatable = false)
    private EmployeeContactStatusEnum contactStatus;
}
