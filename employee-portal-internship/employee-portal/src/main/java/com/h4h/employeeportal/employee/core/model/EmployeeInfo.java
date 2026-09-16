package com.h4h.employeeportal.employee.core.model;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import com.h4h.employeeportal.employee.core.enumeration.EmployeeTypeEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "core_employee_info")
public class EmployeeInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Column(nullable = false)
    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private EmployeeTypeEnum employeeType;

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Column(nullable = false)
    private EmployeeDegreeEnum degree;

    @Column(nullable = false)
    private String identificationNumber;

    private Period priorExperience;

    @Column(nullable = false)
    private EmployeePositionEnum position;

    @Column(nullable = false, insertable = false, updatable = false)
    private EmployeeInfoStatusEnum infoStatus;
}
