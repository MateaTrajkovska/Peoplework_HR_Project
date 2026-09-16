package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeGenderEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeGenderConverter implements AttributeConverter<EmployeeGenderEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeGenderEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeGenderEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeGenderEnum.fromId(id);
    }
}
