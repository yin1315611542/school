//package com.yanda.school.user.service.impl;
//
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import com.yanda.school.ex.EmosException;
//import com.yanda.school.user.QUser;
//import com.yanda.school.user.User;
//import com.yanda.school.user.repository.UserRepository;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//import java.time.LocalDateTime;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class UserService {
//    @Autowired
//    UserRepository userRepository;
//    @Autowired
//    JPAQueryFactory jpaQueryFactory;
//
//    @Value("${wx.app-id}")
//    private String appId;
//
//    @Value("${wx.app-secret}")
//    private String appSecret;
//
//    public User createUser(User user){
//        User save = userRepository.save(user);
//        return save;
//    }
//
//    //注册用户
//    public Long registerUser(String registerCode, String code, String nickname, String photo) {
//        //如果邀请码是000000，代表是超级管理员
//        if (registerCode.equals("000000")) {
//            //查询超级管理员帐户是否已经存在，此小程序只能有一个管理员
//            boolean bool = userRepository.haveRootUser();
//            if (!bool) {
//                //把当前用户绑定到ROOT帐户
//                String openId = getOpenId(code);
//                User user = new User();
//                user.setNickName(nickname);
//                user.setOpenid(openId);
//                user.setRole("[0]");
//                user.setRoot(true);
//                user.setCreateTime(LocalDateTime.now());
//                user.setStatus(1);
//                user.setAvatarUrl(photo);
//                userRepository.save(user);
//                Long id = userRepository.searchByOpenId(openId);
//                return id;
//            } else {
//                //如果root已经绑定了，就抛出异常
//                throw new EmosException("无法绑定超级管理员账号");
//            }
//        }
//        //普通用户注册
//        else{
//            String openId = getOpenId(code);
//            User user = new User();
//            user.setNickName(nickname);
//            user.setOpenid(openId);
////            user.setRole("[0]");
//            user.setRoot(false);
//            user.setCreateTime(LocalDateTime.now());
//            user.setStatus(1);
//            user.setAvatarUrl(photo);
//            User save = userRepository.save(user);
//            return save.getId();
//        }
//    }
//
//
//
//    public User queryUser(String openid){
//        List<User> fetch = jpaQueryFactory.selectFrom(QUser.user).where(QUser.user.openid.eq(openid)).fetch();
//        if (ObjectUtils.isEmpty(fetch)){
//            return null;
//        }
//        return fetch.get(0);
//    }
//
//    public User queryUserId(Long id){
//        List<User> fetch = jpaQueryFactory.selectFrom(QUser.user).where(QUser.user.id.eq(id)).fetch();
//        if (ObjectUtils.isEmpty(fetch)){
//            return null;
//        }
//        return fetch.get(0);
//    }
//
//    public User modifyUser(User user){
//        User save = userRepository.save(user);
//        return save;
//    }
//
//    private String getOpenId(String code) {
//        String url = "https://api.weixin.qq.com/sns/jscode2session";
//        HashMap map = new HashMap();
//        map.put("appid", appId);
//        map.put("secret", appSecret);
//        map.put("js_code", code);
//        map.put("grant_type", "authorization_code");
//        String response = HttpUtil.post(url, map);
//        JSONObject json = JSONUtil.parseObj(response);
//        String openId = json.getStr("openid");
//        if (openId == null || openId.length() == 0) {
//            throw new RuntimeException("临时登陆凭证错误");
//        }
//        return openId;
//    }
//}
