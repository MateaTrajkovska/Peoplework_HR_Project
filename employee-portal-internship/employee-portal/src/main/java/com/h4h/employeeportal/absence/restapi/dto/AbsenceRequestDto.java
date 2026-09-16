package com.h4h.employeeportal.absence.restapi.dto;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
import com.h4h.employeeportal.absence.core.enumeration.AbsenceRequestStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceRequestDto {

    private UUID id;
    private String decisionNumber;
    private LocalDate decisionDate;
    private LocalDate fromDate;
    private LocalDate toDate;
    private Integer daysUsed;
    private AbsenceTypeEnum type;
    private AbsenceRequestStatusEnum absenceRequestStatus;
    private Integer daysUsedFromPreviousYear;
    private Integer daysUsedFromCurrentYear;
    private UUID absenceId;
}