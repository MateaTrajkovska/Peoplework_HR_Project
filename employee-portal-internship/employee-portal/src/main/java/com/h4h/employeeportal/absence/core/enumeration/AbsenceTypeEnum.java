package com.h4h.employeeportal.absence.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AbsenceTypeEnum {
    VACATION(1, "VAC"),
    MATERNITY_LEAVE(2, "MATL"),
    PATERNITY_LEAVE(3, "PATL"),
    SICK_LEAVE(4, "SICL"),
    MEDICAL_LEAVE(5, "MEDL"),
    PAID_LEAVE(6, "PAID");

    private final Integer id;
    private final String abbreviation;

    public static AbsenceTypeEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(absenceTypeEnum -> absenceTypeEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown AbsenceType id: " + id));
    }
}
