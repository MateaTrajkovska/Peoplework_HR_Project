package com.h4h.employeeportal.absence.restapi.dto.create;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateAbsenceDto {

    @NotNull
    private UUID employeeId;

    @NotNull
    private Integer year;
}