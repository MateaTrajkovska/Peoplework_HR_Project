package com.h4h.employeeportal.absence.core.model;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import com.h4h.employeeportal.shared.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(
        name = "abs_absence",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_absence_employee_year",
                        columnNames = {
                                "employee_uuid",
                                "valid_from"
                        }
                )
        }
)
public class Absence extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private Integer maxDays;

    @Column(nullable = false)
    private Integer usedDays;

    @Column(nullable = false)
    private AbsenceStatusEnum absenceStatus;

    @Column(nullable = false)
    private LocalDate validFrom;

    @Column(nullable = false)
    private LocalDate validTo;

    @Column(nullable = false)
    private UUID employeeUuid;

    @Transient
    public Integer getYear() {
        return validFrom != null ? validFrom.getYear() : null;
    }
}
