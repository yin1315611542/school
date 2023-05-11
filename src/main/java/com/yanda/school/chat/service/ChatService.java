package com.yanda.school.chat.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanda.school.chat.Chat;
import com.yanda.school.chat.repository.ChatRepository;
import com.yanda.school.chat.QChat;
import com.yanda.school.user.pojo.TbUser;
import com.yanda.school.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;
    @Autowired
    JPAQueryFactory jpaQueryFactory;
    @Autowired
    UserService userService;

    public void save(Chat chat){
        chatRepository.save(chat);
    }

    public  Map<Integer,List<Chat>>getChatByRecordUser(Integer id){
        List<Chat> fetch = jpaQueryFactory.selectFrom(QChat.chat).where(QChat.chat.userId.eq(id)).fetch();
        Map<Integer, List<Chat>> collect = fetch.stream().collect(Collectors.groupingBy(Chat::getRecordId));
        return collect;
    }

    public List<Chat> getChatByUserIdAndRecordId(Integer userId,Integer recordId){
        List<Chat> fetch = jpaQueryFactory.selectFrom(QChat.chat).where(
                QChat.chat.userId.eq(userId).and(QChat.chat.recordId.eq(recordId))
                        .or(QChat.chat.userId.eq(recordId).and(QChat.chat.recordId.eq(userId)))
        ).fetch();

        return fetch;
    }
    public List<TbUser> getUserForChat(Integer id){
        Map<Integer, List<Chat>> chatByRecordUser = getChatByRecordUser(id);
        List<Integer>  userIds = new ArrayList<>();
        for (Integer userid: chatByRecordUser.keySet()) {
            userIds.add(userid);
        }
        List<TbUser> tbUsers = userService.searchByIds(userIds);
        return tbUsers;
    }
}

