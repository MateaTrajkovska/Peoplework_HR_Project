package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.BankStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BankStatusConverter implements AttributeConverter<BankStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(BankStatusEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public BankStatusEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : BankStatusEnum.fromId(id);
    }
}
