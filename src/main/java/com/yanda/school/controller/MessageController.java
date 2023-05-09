package com.yanda.school.controller;

import com.yanda.school.auth.JwtUtil;
import com.yanda.school.message.MessageEntity;
import com.yanda.school.message.MessageService;
import com.yanda.school.message.MessageTask;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
@Api("消息模块网络接口")
public class MessageController {
    @Autowired
    private JwtUtil jwtUtil;



   @Autowired
    MessageTask messageTask;
   @GetMapping("/messageTest")
   public void test(Integer id){
       MessageEntity messageEntity = new MessageEntity();
       messageEntity.setMsg("safcasfsa");
       messageTask.send(id.toString(),messageEntity);
   }

   @GetMapping("/messageReceive")
    public void receiveAsync(Integer id){
       messageTask.receive(id.toString());
   }

}
