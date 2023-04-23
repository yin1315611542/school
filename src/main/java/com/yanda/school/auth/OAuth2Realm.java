package com.yanda.school.auth;

import com.yanda.school.user.User;
import com.yanda.school.user.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
OAuth2Realm类是AuthorizingRealm的实现类，我们要在这个实现类中定义认证和授权的方法。
因为认证与授权模块设计到用户模块和权限模块，现在我们还没有真正的开发业务模块，
所以我们这里先暂时定义空的认证去授权方法，把Shiro和JWT整合起来，在后续章节我们再实现认证与授权。
 **/
@Component
public class OAuth2Realm extends AuthorizingRealm {



    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;


    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //TODO 查询用户的权限列表
        //TODO 把权限列表添加到info对象中
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //TODO 从令牌中获取userId，然后检测该账户是否被冻结。
        String accessToken=(String)token.getPrincipal();
        Long userId=jwtUtil.getUserId(accessToken);
        User user=userService.queryUserId(userId);

        if(user==null){
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        SimpleAuthenticationInfo info=new SimpleAuthenticationInfo(user,accessToken,getName());
        return info;
    }
}

