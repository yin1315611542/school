package com.yanda.school.controller;

import com.yanda.school.auth.JwtUtil;
import com.yanda.school.order.OrderForm;
import com.yanda.school.publish.Publish;
import com.yanda.school.publish.PublishVo;
import com.yanda.school.publish.mapper.PublishMapper;
import com.yanda.school.publish.service.PublishService;
import com.yanda.school.user.User;
import com.yanda.school.utils.R;
import com.yanda.school.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("order")
public class OrderController {
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    PublishService publishService;
    @Autowired
    PublishMapper publishMapper;

    @PostMapping("receive")
    public R receiveOrder(@RequestBody OrderForm orderForm,HttpServletRequest request){
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);
            Publish publish = publishService.queryPublishById(orderForm.getId());
            publish.setReceiver(userId);
            publishService.createPublish(publish);
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }

    @GetMapping("/myReceiver")
    public R myReceiver(ServletRequest request){
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);
            User user = new User();
            user.setId(userId);
            List<Publish> publishes = publishService.queryPublishByReceiver(user);
            List<PublishVo> collect = publishes.stream().map(e -> publishMapper.entityToVo(e)).collect(Collectors.toList());
            return R.ok().put("data",collect);
        }catch (Exception e){
            return R.error();
        }
    }

    @PostMapping("delReceiver")
    public R cancelOrder(@RequestBody Publish publish){
        try {
            publishService.cancelOrder(publish.getId());
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }

    @PostMapping("completeOrder")
    public R completeOrder(@RequestBody Publish publish){
        try {
            publishService.ModifyPublish(publish);
            return R.ok();
        }catch (Exception e){
            return R.error();
        }
    }


}
