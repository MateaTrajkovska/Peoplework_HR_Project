package com.h4h.employeeportal.employee.core.enumeration;

import com.h4h.employeeportal.shared.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum EmployeeInfoStatusEnum {
    ACTIVE(1),
    INACTIVE(-1);

    private final int id;

    public static EmployeeInfoStatusEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(employeeInfoStatus -> employeeInfoStatus.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Unknown EmployeeInfo id: " + id));
    }
}
