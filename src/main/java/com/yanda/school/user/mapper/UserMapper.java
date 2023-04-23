package com.yanda.school.user.mapper;

import com.yanda.school.publish.Publish;
import com.yanda.school.publish.PublishVo;
import com.yanda.school.user.User;
import com.yanda.school.user.UserVo;
import org.springframework.util.ObjectUtils;

public class UserMapper {
    public UserVo entityToVo(User user){
        UserVo userVo = new UserVo();
        userVo.setId(user.getId());
        userVo.setCode(ObjectUtils.isEmpty(user.getCode())?"":user.getCode());
        userVo.setAvatarUrl(ObjectUtils.isEmpty(user.getAvatarUrl())?"":user.getAvatarUrl());
        userVo.setGender(ObjectUtils.isEmpty(user.getGender())?1:user.getGender());
        userVo.setUsername(ObjectUtils.isEmpty(user.getUsername())?"":user.getUsername());
        userVo.setPassword(ObjectUtils.isEmpty(user.getPassword())?"":user.getPassword());
        userVo.setPhoneNumber(ObjectUtils.isEmpty(user.getPhoneNumber())?"":user.getPhoneNumber());
        userVo.setOpenid(ObjectUtils.isEmpty(user.getOpenid())?"":user.getOpenid());
        userVo.setNickName(ObjectUtils.isEmpty(user.getNickName())?"":user.getNickName());
        return userVo;
    }
}
