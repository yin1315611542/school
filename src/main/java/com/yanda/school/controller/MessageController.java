package com.yanda.school.controller;

import com.yanda.school.auth.JwtUtil;
import com.yanda.school.message.MessageEntity;
import com.yanda.school.message.MessageService;
import com.yanda.school.message.MessageTask;
import com.yanda.school.utils.R;
import com.yanda.school.utils.TokenUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/message")
@Api("消息模块网络接口")
public class MessageController {
   @Autowired
   private JwtUtil jwtUtil;
   @Autowired
   MessageTask messageTask;
   @Autowired
    MessageService messageService;


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

   //添加
    @PostMapping("save")
    public R save(@RequestBody MessageEntity messageEntity){
       messageService.insertMessage(messageEntity);
       return R.ok();
    }

    @PostMapping("delete")
    public R delete(@RequestBody MessageEntity messageEntity){
       messageService.deleteMessageRefById(messageEntity.getId());
       return R.ok();
    }

    @GetMapping("update")
    public R updateRead(@RequestBody MessageEntity messageEntity){
        messageService.updateUnreadMessage(messageEntity.getId());
        return R.ok();
    }

    @GetMapping("query")
    public R query(HttpServletRequest request){
        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
        Long userId = jwtUtil.getUserId(requestToken);
        messageService.searchMessageById(userId.intValue());
    }




}
