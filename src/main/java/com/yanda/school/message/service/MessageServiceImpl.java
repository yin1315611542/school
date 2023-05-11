package com.yanda.school.message.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanda.school.message.QMessageEntity;
import com.yanda.school.message.MessageEntity;
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
    public MessageEntity searchMessageById(Integer id) {
        MessageEntity fetch = jpaQueryFactory.selectFrom(QMessageEntity.messageEntity)
                .where(QMessageEntity.messageEntity.id.eq(id))
                .fetch().get(0);
        return fetch;
    }

    @Override
    public List<MessageEntity> searchMessageByUserId(Integer id) {
        List<MessageEntity> fetch = jpaQueryFactory.selectFrom(QMessageEntity.messageEntity)
                .where(QMessageEntity.messageEntity.senderId.eq(id))
                .fetch();
        return fetch;
    }

    @Override
    public long updateUnreadMessage(Integer id) {
        MessageEntity fetch = jpaQueryFactory.selectFrom(QMessageEntity.messageEntity)
                .where(QMessageEntity.messageEntity.id.eq(id))
                .fetch().get(0);
        fetch.setReadMark(1);
         messageRepository.save(fetch);
//        jpaQueryFactory.update(QMessageEntity.messageEntity).set(QMessageEntity.messageEntity.readMark,1).where(QMessageEntity.messageEntity.id.eq(id)).execute();
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
