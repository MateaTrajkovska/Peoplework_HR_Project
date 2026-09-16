package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeePositionEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeePositionConverter implements AttributeConverter<EmployeePositionEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeePositionEnum position) {
        return position == null ? null : position.getId();
    }

    @Override
    public EmployeePositionEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeePositionEnum.fromId(id);
    }
}
