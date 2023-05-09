package com.yanda.school.controller;

import com.yanda.school.auth.JwtUtil;
import com.yanda.school.chat.Chat;
import com.yanda.school.chat.ChatService;
import com.yanda.school.user.pojo.TbUser;
import com.yanda.school.utils.R;
import com.yanda.school.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("chat")
public class ChatController {
    @Autowired
    ChatService chatService;
    @Autowired
    JwtUtil jwtUtil;
    @PostMapping("save")
    public R saveChat(@RequestBody Chat chat, HttpServletRequest request){
        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
        Long userId = jwtUtil.getUserId(requestToken);
        chat.setUserId(userId.intValue());
        chatService.save(chat);
        return R.ok();
    }

    @GetMapping("chatUser")
    public R getUserForChat(HttpServletRequest request){
        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
        Long userId = jwtUtil.getUserId(requestToken);
        List<TbUser> userForChat = chatService.getUserForChat(userId.intValue());
        userForChat.forEach(e->e.setTel("会话"));
        return R.ok().put("data",userForChat);
    }

    @PostMapping("chat")
    public R getChat( @RequestBody Chat chat, ServletRequest request){
        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
        Long userId = jwtUtil.getUserId(requestToken);
        List<Chat> chatByUserIdAndRecordId = chatService.getChatByUserIdAndRecordId(userId.intValue(), chat.getRecordId());
        List<Chat> collect = chatByUserIdAndRecordId.stream().map(e -> {
            if (e.getUserId().equals(userId.intValue())) {
                e.setUserContent(e.getBotContent());
                e.setBotContent("");
            }else{
                e.setUserContent("");
            }
            return e;
        }).collect(Collectors.toList());
        return R.ok().put("data",collect);
    }
}
