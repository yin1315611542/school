package com.yanda.school.validation.format.validator;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.format.FormatValidator;
import org.springframework.util.ObjectUtils;

public class DateTimeValidation extends FormatValidator<Publish> {

    @Override
    public ValidateInfo doValidate(Publish publish) {
        if (ObjectUtils.isEmpty(publish.getEndOfTime())){
            return new ValidateInfo(true,"结束时间不可为空");
        }
        if (ObjectUtils.isEmpty(publish.getStartingTime())){
            return new ValidateInfo(true,"起始时间不可为空");
        }
        return new ValidateInfo(false,"验证成功");
    }
}
