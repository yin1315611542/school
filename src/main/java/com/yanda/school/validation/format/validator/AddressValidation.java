package com.yanda.school.validation.format.validator;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.format.FormatValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AddressValidation extends FormatValidator<Publish> {
    private static List<String> addressList = null;
    @Override
    public ValidateInfo doValidate(Publish publish) {
        if (ObjectUtils.isEmpty(publish.getStartingPlace())){
            return new ValidateInfo(true,"起始地点不可为空");
        }
        if (ObjectUtils.isEmpty(publish.getDestination())){
            return new ValidateInfo(true,"目的地不可为空");
        }
        if (!addressList.contains(publish.getDestination())){
            return new ValidateInfo(true,"目的地不在配送范围内");
        }
        if (!addressList.contains(publish.getStartingPlace())){
            return new ValidateInfo(true,"起始地不在配送范围内");
        }
        return new ValidateInfo(false,"地点校验成功");
    }

    @PostConstruct
    public void Initialize(){
        this.addressList = Arrays.asList("三元湖","一教","二教","三教");
    }
}
