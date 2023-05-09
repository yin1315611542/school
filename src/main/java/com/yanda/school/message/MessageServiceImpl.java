package com.yanda.school.message;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {


    @Override
    public String insertMessage(MessageEntity entity) {
        return null;
    }

    @Override
    public String insertRef(MessageRefEntity entity) {
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
    public HashMap searchMessageById(String id) {
        return null;
    }

    @Override
    public long updateUnreadMessage(String id) {
        return 0;
    }

    @Override
    public long deleteMessageRefById(String id) {
        return 0;
    }

    @Override
    public long deleteUserMessageRef(int userId) {
        return 0;
    }
}
