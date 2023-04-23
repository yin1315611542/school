package com.yanda.school.moudel;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.ValidateInfo;
import com.yanda.school.validation.Validator;
import com.yanda.school.validation.format.FormatValidator;
import com.yanda.school.validation.format.validator.AddressValidation;
import com.yanda.school.validation.format.validator.PhoneNumberValidation;
import com.yanda.school.validation.format.validator.TitleValidation;
import org.springframework.stereotype.Component;

@Component
public class ModuleProvider {

    public FormatValidator<Publish> getValidator(ModuleType moduleType){

        if (ModuleType.idle.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            AddressValidation addressValidation = new AddressValidation();
            PhoneNumberValidation phoneNumberValidation = new PhoneNumberValidation();
            phoneNumberValidation.setNextFormatValidator(addressValidation);
            titleValidation.setNextFormatValidator(phoneNumberValidation);
            return titleValidation;
        }

        if (ModuleType.Courier.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            AddressValidation addressValidation = new AddressValidation();
            PhoneNumberValidation phoneNumberValidation = new PhoneNumberValidation();
            phoneNumberValidation.setNextFormatValidator(addressValidation);
            titleValidation.setNextFormatValidator(phoneNumberValidation);
            return titleValidation;
        }

        if (ModuleType.lostAndFound.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            AddressValidation addressValidation = new AddressValidation();
            PhoneNumberValidation phoneNumberValidation = new PhoneNumberValidation();
            phoneNumberValidation.setNextFormatValidator(addressValidation);
            titleValidation.setNextFormatValidator(phoneNumberValidation);
            return titleValidation;
        }

        if (ModuleType.mindPost.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            return titleValidation;
        }

        if (ModuleType.takeout.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            AddressValidation addressValidation = new AddressValidation();
            PhoneNumberValidation phoneNumberValidation = new PhoneNumberValidation();
            phoneNumberValidation.setNextFormatValidator(addressValidation);
            titleValidation.setNextFormatValidator(phoneNumberValidation);
            return titleValidation;
        }

        if (ModuleType.reservation.getCode().equals(moduleType.getCode())){
            TitleValidation titleValidation = new TitleValidation();
            AddressValidation addressValidation = new AddressValidation();
            PhoneNumberValidation phoneNumberValidation = new PhoneNumberValidation();
            phoneNumberValidation.setNextFormatValidator(addressValidation);
            titleValidation.setNextFormatValidator(phoneNumberValidation);
            return titleValidation;
        }
        return null;
    }

}
