package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
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
@Table(name = "core_employee_finance")
public class EmployeeFinance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Column(nullable = false)
    private Long bankAccount;

    @Column(nullable = false, insertable = false, updatable = false)
    private BankStatusEnum bankStatus;
}
