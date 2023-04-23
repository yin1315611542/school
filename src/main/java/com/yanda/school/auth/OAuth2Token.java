package com.yanda.school.auth;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 客户端提交的Token不能直接交给Shiro框架，需要先封装成AuthenticationToken类型的对象，所以我们我们需要先创建AuthenticationToken的实现类。
 */
public class OAuth2Token implements AuthenticationToken {
    private String token;

    public OAuth2Token(String token){
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

