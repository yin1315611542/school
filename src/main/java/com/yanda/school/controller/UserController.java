package com.yanda.school.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.yanda.school.auth.JwtUtil;
import com.yanda.school.user.LoginForm;
import com.yanda.school.user.pojo.DeleteUserByIdForm;
import com.yanda.school.user.pojo.RegisterForm;
import com.yanda.school.user.pojo.SearchUserInfoForm;
import com.yanda.school.user.pojo.UpdateUserInfoForm;
import com.yanda.school.user.service.UserService;
import com.yanda.school.utils.EmosException;
import com.yanda.school.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("school/user")
@Api("用户模块Web接口")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${emos.jwt.cache-expire}")
    private int cacheExpire;


    @PostMapping("/register")
    @ApiOperation("注册用户")
    public R  register(@Valid @RequestBody RegisterForm form) {
        int id = userService.registerUser(form.getRegisterCode(), form.getCode(), form.getNickname(), form.getPhoto());
        String token = jwtUtil.createToken(id);
        //根据用户返回用户的权限列表
        Set<String> permsSet = userService.searchUserPermissions(id);
        saveCacheToken(token, id);
        return R.ok("用户注册成功").put("token", token).put("permission", permsSet);
    }



    @PostMapping("/login")
    @ApiOperation("登录系统")
    public R login(@Valid @RequestBody LoginForm form){
        //生成token
        Integer id = userService.login(form.getCode());
        String token = jwtUtil.createToken(id);
        //查询该用户的操作权限
        Set<String> permission = userService.searchUserPermissions(id);
        //保存到Redis中
        saveCacheToken(token,id);
        //返回给前端
        return R.ok("登录成功").put("token",token).put("permission",permission);
    }


    @GetMapping("/searchUserSummary")
    @ApiOperation("查询用户摘要信息")
    public R searchUserSummary(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token).intValue();
        HashMap map = userService.searchUserSummary(userId);
        return R.ok().put("result", map);
    }


    @PostMapping("/searchUserInfo")
    @ApiOperation("查询员工数据")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:SELECT"}, logical = Logical.OR)
    public R searchUserInfo(@Valid @RequestBody SearchUserInfoForm form) {
        HashMap map = userService.searchUserInfo(form.getUserId());
        return R.ok().put("result", map);
    }

    @GetMapping("/searchUserSelfInfo")
    @ApiOperation("查询用户信息")
    public R searchUserSelfInfo(@RequestHeader("token") String token) {
        int userId = jwtUtil.getUserId(token).intValue();
        HashMap map = userService.searchUserInfo(userId);
        return R.ok().put("result", map);
    }


    @PostMapping("/updateUserInfo")
    @ApiOperation("更新用户数据")
//    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:UPDATE"}, logical = Logical.OR)
    public R updateUserInfo(@Valid @RequestBody UpdateUserInfoForm form) {
        boolean root = false;

        HashMap param = new HashMap();
        param.put("name", form.getName());
        param.put("sex", form.getSex());
        param.put("tel", form.getTel());
        param.put("email", form.getEmail());
//        param.put("hiredate", form.getHiredate());
        param.put("status", form.getStatus());
        param.put("userId", form.getUserId());
        param.put("root", root);

        int rows = userService.updateUserInfo(param);
        return R.ok().put("result", rows);
    }

    @PostMapping("/deleteUserById")
    @ApiOperation("删除员工记录")
    @RequiresPermissions(value = {"ROOT", "EMPLOYEE:DELETE"}, logical = Logical.OR)
    public R deleteUserById(@Valid @RequestBody DeleteUserByIdForm form) {
        userService.deleteUserById(form.getId());
//        HashMap param=new HashMap();
//        param.put("status",2);
//        param.put("userId",form.getId());
//        userService.updateUserStatus(param);
        return R.ok().put("result", "success");
    }
    //redis中用户信息存储
    private void saveCacheToken(String token, int userId) {
        redisTemplate.opsForValue().set(token, userId + "", cacheExpire, TimeUnit.DAYS);
    }
}
