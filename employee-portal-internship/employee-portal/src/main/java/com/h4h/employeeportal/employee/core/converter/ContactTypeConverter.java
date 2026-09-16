package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeContactTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ContactTypeConverter implements AttributeConverter<EmployeeContactTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeContactTypeEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeContactTypeEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeContactTypeEnum.fromId(id);
    }
}
