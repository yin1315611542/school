package com.yanda.school.user.service.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yanda.school.message.MessageEntity;
import com.yanda.school.user.QUser;
import com.yanda.school.user.User;
import com.yanda.school.user.mapper.TbUserDao;
import com.yanda.school.user.pojo.TbUser;
import com.yanda.school.user.service.UserService;
import com.yanda.school.utils.EmosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@Scope("prototype")
public class UserServiceImpl implements UserService {
    @Value("${wx.app-id}")
    private String appId;

    @Value("${wx.app-secret}")
    private String appSecret;

    @Autowired
    private TbUserDao userDao;
    @Autowired
    JPAQueryFactory jpaQueryFactory;


    @Autowired
    private RedisTemplate redisTemplate;

    private String getOpenId(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        HashMap map = new HashMap();
        map.put("appid", appId);
        map.put("secret", appSecret);
        map.put("js_code", code);
        map.put("grant_type", "authorization_code");
        String response = HttpUtil.post(url, map);
        JSONObject json = JSONUtil.parseObj(response);
        String openId = json.getStr("openid");
        if (openId == null || openId.length() == 0) {
            throw new RuntimeException("临时登陆凭证错误");
        }
        return openId;
    }

    @Override
    public int registerUser(String registerCode, String code, String nickname, String photo) {
        if (registerCode.equals("000000")) {
            boolean bool = userDao.haveRootUser();
            if (!bool) {
                String openId = getOpenId(code);
                HashMap param = new HashMap();
                param.put("openId", openId);
                param.put("nickname", nickname);
                param.put("photo", photo);
                param.put("role", "[0]");
                param.put("status", 1);
                param.put("createTime", new Date());
                param.put("root", true);
                userDao.insert(param);
                int id = userDao.searchIdByOpenId(openId);
                return id;
            } else {
                throw new EmosException("无法绑定超级管理员账号");
            }

        } else {
            String openId = getOpenId(code);
            HashMap param = new HashMap();
            param.put("openId", openId);
            param.put("nickname", nickname);
            param.put("photo", photo);
            param.put("role", "[4]");
            param.put("status", 1);
            param.put("createTime", new Date());
            param.put("root", false);
            int row = userDao.insert(param);
            if (row != 1) {
                throw new EmosException("");
            }
            return row;
        }
    }

    @Override
    public Set<String> searchUserPermissions(int userId) {
        Set<String> permissions = userDao.searchUserPermissions(userId);
        return permissions;
    }

    @Override
    public Integer login(String code) {
        String openId = getOpenId(code);
        Integer id = userDao.searchIdByOpenId(openId);
        if (id == null) {
            throw new EmosException("帐户不存在");
        }
        return id;
    }

    @Override
    public TbUser searchById(int userId) {
        TbUser user = userDao.searchById(userId);
        return user;
    }
   @Override
    public List<TbUser> searchByIds(List ids){
        return userDao.searchByIds(ids);
    }

    @Override
    public String searchUserHiredate(int userId) {
        String hiredate = userDao.searchUserHiredate(userId);
        return hiredate;
    }

    @Override
    public HashMap searchUserSummary(int userId) {
        HashMap map = userDao.searchUserSummary(userId);
        return map;
    }


    @Override
    public ArrayList<HashMap> searchMembers(List param) {
        ArrayList<HashMap> list = userDao.searchMembers(param);
        return list;
    }

    @Override
    public List<HashMap> selectUserPhotoAndName(List param) {
        List<HashMap> list = userDao.selectUserPhotoAndName(param);
        return list;
    }

    @Override
    public String searchMemberEmail(int id) {
        String email = userDao.searchMemberEmail(id);
        return email;
    }

    @Override
    public void insertUser(HashMap param) {
        //保存记录
        int row = userDao.insert(param);
        if (row == 1) {
            String email = (String) param.get("email");
            //根据Email查找新添加用户的主键值
            int userId = userDao.searchUserIdByEmail(email);
        } else {
            throw new EmosException("员工数据添加失败");
        }
    }

    @Override
    public HashMap searchUserInfo(int userId) {
        HashMap map = userDao.searchUserInfo(userId);
        return map;
    }

    @Override
    public int updateUserInfo(HashMap param) {
        //更新用户记录
        int rows = userDao.updateUserInfo(param);
        //更新成功就发送消息通知 暂时不用
        if (rows == 1) {
            Integer userId = (Integer) param.get("userId");
            String msg = "你的个人资料已经被成功修改";
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.setMsg(msg);
        }
        return rows;
    }

    @Override
    public void deleteUserById(int id) {
        int row = userDao.deleteUserById(id); //删除员工数据
        if (row != 1) {
            throw new EmosException("删除员工失败");
        }
    }
    public List<User> searchAllUser(){
        return jpaQueryFactory.selectFrom(QUser.user).where(QUser.user.status.eq(1)).fetch();
    }

}
