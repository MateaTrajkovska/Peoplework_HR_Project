package com.h4h.employeeportal.absence.core.converter;

import com.h4h.employeeportal.absence.core.enumeration.AbsenceTypeEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AbsenceTypeEnumConverter implements AttributeConverter<AbsenceTypeEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(AbsenceTypeEnum status) {
        return status == null ? null : status.getId();
    }

    @Override
    public AbsenceTypeEnum convertToEntityAttribute(Integer id) {
        return id == null ? null : AbsenceTypeEnum.fromId(id);
    }
}
