package com.h4h.employeeportal.employee.core.converter;

import com.h4h.employeeportal.employee.core.enumeration.LanguageEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<LanguageEnum, String> {

    @Override
    public String convertToDatabaseColumn(LanguageEnum language) {
        return language == null ? null : language.name().toLowerCase();
    }

    @Override
    public LanguageEnum convertToEntityAttribute(String value) {
        return value == null ? null : LanguageEnum.valueOf(value.toUpperCase());
    }
}
