package com.yanda.school.validation.format.validator;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.format.FormatValidator;

public class PhoneNumberValidation extends FormatValidator<Publish> {
    //电话号码正则表达式
    private String regex = "^((\\+86)|(86))?(13[0-9]|14[5-9]|15[0-3,5-9]|16[6]|17[0-8]|18[0-9]|19[1,8,9])\\d{8}$";
    @Override
    public ValidateInfo doValidate(Publish publish) {
        String phoneNumber = publish.getMobilePhoneNo();
        phoneNumber = phoneNumber.replaceAll("[\\s\\-()]", "");
        if (publish.getMobilePhoneNo() == null){
            return new ValidateInfo(true,"电话号码不能为空");
        }
        if (!phoneNumber.matches(regex)){
            return new ValidateInfo(true,"电话号码格式不正确");
        }
        return new ValidateInfo(false,"验证成功");
    }
}
