package com.yanda.school.publish.mapper;

import com.yanda.school.publish.Publish;
import com.yanda.school.publish.PublishVo;
import com.yanda.school.user.service.UserService;
import com.yanda.school.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PublishMapper {
   @Autowired
   UserService userService;
    public PublishVo entityToVo(Publish publish){
        PublishVo publishVo = new PublishVo();
        publishVo.setContent(publish.getContent());
        publishVo.setDestination(publish.getDestination());
        publishVo.setImg(publish.getImg());
        publishVo.setId(publish.getId());
        publishVo.setStartingPlace(publish.getStartingPlace());
        publishVo.setEndOfTime(publish.getEndOfTime());
        publishVo.setMobilePhoneNo(publish.getMobilePhoneNo());
        publishVo.setType(publish.getType().getCode());
        publishVo.setTitle(publish.getTitle());
        publishVo.setStartingTime(publish.getStartingTime());
        publishVo.setPhoto(getPhoto(publish.getPublisher()));
        return publishVo;
    }
    //获取头像
    public String getPhoto(Long id){
        return userService.searchById(id.intValue()).getPhoto();
    }
}
