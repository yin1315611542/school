package com.yanda.school.validation.format.validator;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.format.FormatValidator;
import org.springframework.util.ObjectUtils;

public class TitleValidation extends FormatValidator<Publish> {

    @Override
    public ValidateInfo doValidate(Publish publish) {
        if (ObjectUtils.isEmpty(publish.getTitle())){
            return new ValidateInfo(true,"标题不可为空");
        }
       return new ValidateInfo(false,"验证成功");
    }
}
