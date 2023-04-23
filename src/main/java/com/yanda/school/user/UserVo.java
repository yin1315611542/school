package com.yanda.school.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    Long id;

    String nickName;

    Integer gender;

    String username;

    String password;

    String phoneNumber;

    String openid;

    String avatarUrl;

    String code;
}
