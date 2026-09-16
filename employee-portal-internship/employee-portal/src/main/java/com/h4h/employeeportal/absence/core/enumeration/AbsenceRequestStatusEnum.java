package com.h4h.employeeportal.absence.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum AbsenceRequestStatusEnum {

    PENDING(0),
    APPROVED(1),
    DENIED(-1),
    CANCELLED(-2);

    private final Integer id;

    public static AbsenceRequestStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(status -> status.getId() == id)
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "absenceRequestStatus.notFound"
                        )
                );
    }
}
