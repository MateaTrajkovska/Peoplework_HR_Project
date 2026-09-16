package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeReportStatusEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "core_employee_report")
public class EmployeeReport extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Size(max = 1000)
    @Column(length = 1000)
    private String internalNote;

    @Column(nullable = false, insertable = false, updatable = false)
    private EmployeeReportStatusEnum reportStatus;
}
