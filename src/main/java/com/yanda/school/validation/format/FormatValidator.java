package com.yanda.school.validation.format;

import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.Validator;
import org.springframework.util.ObjectUtils;

public abstract class FormatValidator<T> implements Validator<T> {

    public FormatValidator<T> nextFormatValidator;

    public ValidateInfo formatValidate(T t){

        ValidateInfo validateInfo = this.doValidate(t);
        if (validateInfo.isValidation()){
            return validateInfo;
        }else if(!ObjectUtils.isEmpty(nextFormatValidator)){
             validateInfo = nextFormatValidator.formatValidate(t);
        }
       return validateInfo;
    }

    public void setNextFormatValidator(FormatValidator<T> validator){
        this.nextFormatValidator = validator;
    }
}
