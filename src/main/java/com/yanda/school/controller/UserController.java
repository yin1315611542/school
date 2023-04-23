package com.yanda.school.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yanda.school.config.BaseGduiDTO;
import com.yanda.school.user.LoginForm;
import com.yanda.school.user.User;
import com.yanda.school.user.UserVo;
import com.yanda.school.user.mapper.UserMapper;
import com.yanda.school.user.service.UserService;
import com.yanda.school.utils.EmosException;
import com.yanda.school.auth.JwtUtil;
import com.yanda.school.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
@RequestMapping("school")
public class UserController {

    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    UserService userService;
    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;

    @PostMapping("/user/login")
    public BaseGduiDTO<?> login(@RequestBody LoginForm userInfo) {
        String code = userInfo.getCode();
        String openId = getOpenId(code);
        User user = userInfo.getUserInfo();
        user.setNickName(getRandomString(7));
        user.setOpenid(openId);
        User user1 = userService.queryUser(openId);
        if (ObjectUtils.isEmpty(user1)) {
            user1 = userService.createUser(user);
        }
        String token = jwtUtil.createToken(user1.getId().intValue());
        redisTemplate.opsForValue().set(token, user1.getId() + "", cacheExpire, TimeUnit.DAYS);
        HashMap<String, Object> maps = new HashMap<>();
        maps.put("token", token);
        UserMapper userMapper = new UserMapper();
        UserVo userVo = userMapper.entityToVo(user1);
        maps.put("user", userVo);
        return BaseGduiDTO.ok(maps);
    }
    @PostMapping("/user/register")
    public BaseGduiDTO<?> register(@RequestBody LoginForm userInfo){

        String openId = getOpenId(userInfo.getCode());
        User user = userInfo.getUserInfo();
        user.setOpenid(openId);
        User user1 = userService.queryUser(openId);
        if (ObjectUtils.isEmpty(user1)) {
            userService.createUser(user);
            return BaseGduiDTO.ok("注册成功");
        }else {
            return BaseGduiDTO.error("用户已存在");
        }

    }




    //length用户要求产生字符串的长度
    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    @PostMapping("user")
    public BaseGduiDTO<?> getUser(ServletRequest request){
        try {
            String requestToken = TokenUtil.getRequestToken((HttpServletRequest) request);
            Long userId = jwtUtil.getUserId(requestToken);
            User user = userService.queryUserId(userId);
            UserMapper userMapper = new UserMapper();
            UserVo userVo = userMapper.entityToVo(user);
            return BaseGduiDTO.ok(userVo);
        }catch (Exception e){
            return BaseGduiDTO.error();
        }
    }

    @PostMapping("updateUser")
    public BaseGduiDTO<?> updateUser(@RequestBody User user) {
        try {
            if (ObjectUtils.isEmpty(user.getId())) {
                throw new EmosException("用户ID为空");
            }
            User user1 = userService.modifyUser(user);
            UserMapper userMapper = new UserMapper();
            UserVo userVo = userMapper.entityToVo(user1);
            return BaseGduiDTO.ok(userVo);
            } catch (Exception e) {
            return BaseGduiDTO.error();
        }

    }

    public void saveCacheToken(String token, int userId) {
        redisTemplate.opsForValue().set(token, userId + "", 100, TimeUnit.DAYS);
    }

    public String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", "wx67f1b14b12cce313");
        map.put("secret", "06b7cdd2c6a9daadc9c4958e9589413c");
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        return openId;
    }

}
