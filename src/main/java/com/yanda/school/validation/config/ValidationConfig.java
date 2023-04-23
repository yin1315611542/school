package com.yanda.school.validation.config;

import com.yanda.school.publish.Publish;
import com.yanda.school.validation.format.FormatValidator;
import com.yanda.school.validation.format.validator.AddressValidation;
import com.yanda.school.validation.format.validator.DateTimeValidation;
import com.yanda.school.validation.format.validator.DescriptionValidation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {
    @Bean("formatValidators")
    public FormatValidator<Publish> createFormatValidator(){
        AddressValidation addressValidation = new AddressValidation();
        DateTimeValidation dateTimeValidation = new DateTimeValidation();
        DescriptionValidation descriptionValidation = new DescriptionValidation();
        descriptionValidation.setNextFormatValidator(dateTimeValidation);
        addressValidation.setNextFormatValidator(descriptionValidation);
        return addressValidation;
    }
}
