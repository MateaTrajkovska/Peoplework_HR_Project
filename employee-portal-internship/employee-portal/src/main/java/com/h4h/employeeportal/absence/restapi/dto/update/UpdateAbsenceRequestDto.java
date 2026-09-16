package com.h4h.employeeportal.absence.restapi.dto.update;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAbsenceRequestDto {

    @NotNull
    private LocalDate fromDate;

    @NotNull
    private LocalDate toDate;
}