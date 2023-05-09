package com.yanda.school.message;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanda.school.message.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    JPAQueryFactory jpaQueryFactory;

    @Override
    public String insertMessage(MessageEntity entity) {
        messageRepository.save(entity);
        return null;
    }


    @Override
    public long searchUnreadCount(int userId) {
        return 0;
    }

    @Override
    public long searchLastCount(int userId) {
        return 0;
    }

    @Override
    public List<HashMap> searchMessageByPage(int userId, long start, int length) {
        return null;
    }

    @Override
    public List<MessageEntity> searchMessageById(Integer id) {
        List<MessageEntity> fetch = jpaQueryFactory.selectFrom(QMessageEntity.messageEntity)
                .where(QMessageEntity.messageEntity.senderId.eq(id))
                .fetch();
        return fetch;
    }

    @Override
    public long updateUnreadMessage(Integer id) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setId(id);
        messageEntity.setRead(1);
        return 0;
    }

    @Override
    public long deleteMessageRefById(Integer id) {
        messageRepository.deleteById(id);
        return 0;
    }

    @Override
    public long deleteUserMessageRef(int userId) {

        return 0;
    }
}
