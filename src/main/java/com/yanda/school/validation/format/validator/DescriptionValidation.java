package com.yanda.school.validation.format.validator;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.format.FormatValidator;
import org.springframework.util.ObjectUtils;

public class DescriptionValidation extends FormatValidator<Publish> {
    @Override
    public ValidateInfo doValidate(Publish publish) {
        if (ObjectUtils.isEmpty(publish.getContent())){
            return new ValidateInfo(true,"内容不可为空");
        }
        return new ValidateInfo(false,"验证通过");
    }
}
