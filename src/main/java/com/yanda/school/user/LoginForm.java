package com.yanda.school.user;

import lombok.Data;

@Data
public class LoginForm {
    private String code;
    private User userInfo;
}
