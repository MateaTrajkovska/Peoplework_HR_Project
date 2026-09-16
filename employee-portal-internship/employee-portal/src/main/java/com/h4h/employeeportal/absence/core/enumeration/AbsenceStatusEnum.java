package com.h4h.employeeportal.absence.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AbsenceStatusEnum {
    ACTIVE(1),
    INACTIVE(-1);

    private final Integer id;

    public static AbsenceStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(absenceStatusEnum -> absenceStatusEnum.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown AbsenceStatus id: " + id));
    }
}
