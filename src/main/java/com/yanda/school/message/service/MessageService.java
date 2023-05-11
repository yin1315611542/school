package com.yanda.school.message.service;

import com.yanda.school.message.MessageEntity;

import java.util.List;

public interface MessageService {

    public String insertMessage(MessageEntity entity);

    public MessageEntity searchMessageById(Integer id);

    public  List<MessageEntity> searchMessageByUserId(Integer userId);

    public long updateUnreadMessage(Integer id) ;

    public long deleteMessageRefById(Integer id);

}
