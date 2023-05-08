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
    public R saveChat(@RequestBody Chat chat){
        chatService.save(chat);
        return R.ok();
    }

    @GetMapping("chatUser")
    public R getUserForChat(HttpServletRequest request){
//        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
//        Long userId = jwtUtil.getUserId(requestToken);
//        List<TbUser> userForChat = chatService.getUserForChat(userId.intValue());
        List<TbUser> userForChat = chatService.getUserForChat(9);
        return R.ok().put("data",userForChat);
    }

    @GetMapping("chat")
    public R getChat( ServletRequest request){
//        String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
//        Long userId = jwtUtil.getUserId(requestToken);
//        List<TbUser> userForChat = chatService.getUserForChat(userId.intValue());
        List<Chat> chatByUserIdAndRecordId = chatService.getChatByUserIdAndRecordId(9, 10);
        List<Chat> collect = chatByUserIdAndRecordId.stream().map(e -> {
            if (e.getUserId().equals(10)) {
                e.setUserContent(e.getBotContent());
                e.setBotContent(null);
            }
            return e;
        }).collect(Collectors.toList());
        return R.ok().put("data",collect);
    }
}
