package com.yanda.school;

import com.yanda.school.chat.ChatService;
import com.yanda.school.moudel.ModuleType;
import com.yanda.school.publish.Publish;
import com.yanda.school.publish.service.PublishService;
import com.yanda.school.user.pojo.TbUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class SchoolApplicationTests {

    @Autowired
    PublishService publishService;
    @Autowired
    ChatService chatService;
    @Test
    void contextLoads() {

        for (int i = 0; i < 6; i++) {
            for (int j = 0;j<20;j++){
                Publish publish = new Publish();
                publish.setContent(i+""+j);
                publish.setDestination("图书馆");
                publish.setImg("");
                publish.setStartingPlace("三元胡");
                publish.setMobilePhoneNo("171711111111");
                publish.setTitle(i+""+j);
                publish.setType(ModuleType.getModuleType(i));
                publish.setStartingTime(LocalDateTime.now());
                publish.setEndOfTime(LocalDateTime.now());
                publishService.createPublish(publish);
            }

        }

    }

    @Test
    void getUser(){
        List<TbUser> userForChat = chatService.getUserForChat(9);
    }

}
