package com.yanda.school.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginForm {
    @NotBlank(message = "临时授权不能为空")
    private String code;
}
