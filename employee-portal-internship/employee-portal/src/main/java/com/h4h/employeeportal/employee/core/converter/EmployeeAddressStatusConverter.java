package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.EmployeeAddressStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmployeeAddressStatusConverter implements AttributeConverter<EmployeeAddressStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EmployeeAddressStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public EmployeeAddressStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : EmployeeAddressStatusEnum.fromId(id);
    }
}
