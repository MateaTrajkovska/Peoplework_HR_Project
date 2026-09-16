package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeInfoStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeInfoStatusConverter implements AttributeConverter<EmployeeInfoStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeInfoStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeInfoStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeInfoStatusEnum.fromId(id);
    }
}
