package com.h4h.employeeportal.absence.restapi.dto.update;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAbsenceDto {

    @NotNull(message = "Status is required")
    private AbsenceStatusEnum absenceStatus;
}