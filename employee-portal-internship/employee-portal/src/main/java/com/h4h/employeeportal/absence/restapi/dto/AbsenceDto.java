package com.h4h.employeeportal.absence.restapi.dto;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
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
public class AbsenceDto {

    private UUID id;
    private Integer year;
    private Integer maxDays;
    private Integer usedDays;
    private AbsenceStatusEnum absenceStatus;
    private LocalDate validFrom;
    private LocalDate validTo;
    private UUID employeeId;
}