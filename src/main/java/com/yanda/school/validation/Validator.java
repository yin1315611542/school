package com.yanda.school.validation;

public interface Validator<T> {
    public  ValidateInfo doValidate(T t);
}
