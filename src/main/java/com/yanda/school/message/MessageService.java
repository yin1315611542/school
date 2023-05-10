package com.yanda.school.message;

import java.util.HashMap;
import java.util.List;

public interface MessageService {

    public String insertMessage(MessageEntity entity);


    public long searchUnreadCount(int userId);

    public long searchLastCount(int userId);

    public List<HashMap> searchMessageByPage(int userId, long start, int length) ;

    public MessageEntity searchMessageById(Integer id);

    public  List<MessageEntity> searchMessageByUserId(Integer userId);

    public long updateUnreadMessage(Integer id) ;

    public long deleteMessageRefById(Integer id);

	public long deleteUserMessageRef(int userId);
}
