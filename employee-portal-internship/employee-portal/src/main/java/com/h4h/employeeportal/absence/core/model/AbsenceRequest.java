package com.h4h.employeeportal.absence.core.model;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
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
@Table(name = "abs_absence_request")
public class AbsenceRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false,unique = true)
    private String decisionNumber;

    @Column(nullable = false)
    private LocalDate decisionDate;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Transient
    private Integer daysUsed;

    @Column(nullable = false)
    private AbsenceTypeEnum type;

    @Column(name = "absence_request_status", nullable = false)
    private AbsenceRequestStatusEnum absenceRequestStatus;

    private Integer daysUsedFromPreviousYear;

    private Integer daysUsedFromCurrentYear;

    @Column(nullable = false)
    private UUID absenceUuid;

    @Column(length = 1000)
    private String reason;
}
