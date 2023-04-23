package com.yanda.school.moudel;

import javax.persistence.AttributeConverter;

public class ModuleTypeConverter implements AttributeConverter<ModuleType, Integer> {


    @Override
    public Integer convertToDatabaseColumn(ModuleType moduleType) {
        return moduleType.getCode();
    }

    @Override
    public ModuleType convertToEntityAttribute(Integer code) {
        return ModuleType.getModuleType(code);
    }
}
