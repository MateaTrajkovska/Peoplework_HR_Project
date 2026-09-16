package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeDegreeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeDegreeConverter implements AttributeConverter<EmployeeDegreeEnum, Integer> {
    @Override
    public Integer convertToDatabaseColumn(EmployeeDegreeEnum degree) {
        return degree == null ? null : degree.getId();
    }

    @Override
    public EmployeeDegreeEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeDegreeEnum.fromId(id);
    }
}
