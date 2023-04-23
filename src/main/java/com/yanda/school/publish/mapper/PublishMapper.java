package com.yanda.school.publish.mapper;

import com.yanda.school.publish.Publish;
import com.yanda.school.publish.PublishVo;

public class PublishMapper {
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
        return publishVo;
    }
}
