package com.yanda.school.controller;

import com.yanda.school.auth.JwtUtil;
import com.yanda.school.message.MessageEntity;
import com.yanda.school.message.MessageTask;
import com.yanda.school.message.service.MessageService;
import com.yanda.school.user.User;
import com.yanda.school.user.service.UserService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 消息模块控制层
 */
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
   @Autowired
    UserService userService;


   @GetMapping("/messageTest")
   public void test(Integer id){
       MessageEntity messageEntity = new MessageEntity();
       messageEntity.setMsg("safcasfsa");
       messageTask.send(id.toString(),messageEntity);
   }

    @PostMapping("/messagePublish")
    public R create(@RequestBody MessageEntity messageEntity){
        List<User> users = userService.searchAllUser();
        List<Long> user = users.stream().map(User::getId).collect(Collectors.toList());
        for (Long userid : user) {
            MessageEntity messageEntity1 = new MessageEntity();
            messageEntity1.setSenderId(userid.intValue());
            messageEntity1.setReadMark(0);
            messageEntity1.setSenderName("校园小助手");
            messageEntity1.setMsg(messageEntity.getMsg());
            messageEntity1.setSendTime(new Date());
            messageService.insertMessage(messageEntity1);
        }
        return R.ok();
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

    @PostMapping("update")
    public R updateRead(@RequestBody MessageEntity messageEntity){
        messageService.updateUnreadMessage(messageEntity.getId());
        return R.ok();
    }

    @GetMapping("query")
    public R query(HttpServletRequest request){
        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
        Long userId = jwtUtil.getUserId(requestToken);
        List<MessageEntity> messageEntities = messageService.searchMessageByUserId(userId.intValue());
        return R.ok().put("data",messageEntities);
    }

    @PostMapping("queryById")
    public R queryId(@RequestBody MessageEntity messageEntity){
         MessageEntity messageEntities = messageService.searchMessageById(messageEntity.getId());
        return R.ok().put("data",messageEntities);
    }
}
