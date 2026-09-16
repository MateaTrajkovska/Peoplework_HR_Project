package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeAddressStatusEnum {
    ACTIVE(1),
    INACTIVE(-1);

    private final int id;

    public static EmployeeAddressStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(contactStatus -> contactStatus.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown contactStatus id: " + id));
    }
}
