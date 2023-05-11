package com.yanda.school.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "tb_user")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String nickName;

    Integer gender;

    String username;

    String password;

    String phoneNumber;

    String openid;

    String avatarUrl;

    String code;

    Boolean root;

    String role;

    Integer status;

    LocalDateTime createTime;
}
