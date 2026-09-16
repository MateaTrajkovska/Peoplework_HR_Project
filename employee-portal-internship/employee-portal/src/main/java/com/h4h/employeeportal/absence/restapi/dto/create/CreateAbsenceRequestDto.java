package com.h4h.employeeportal.absence.restapi.dto.create;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
import jakarta.validation.constraints.NotNull;
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
public class CreateAbsenceRequestDto {

    @NotNull
    private LocalDate decisionDate;

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;

    @NotNull
    private AbsenceTypeEnum type;

    @NotNull
    private UUID absenceId;
}