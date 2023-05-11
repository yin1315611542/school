package com.yanda.school.auth;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.yanda.school.utils.EmosException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * JWT的Token要经过加密才能返回给客户端，包括客户端上传的Token，后端项目需要验证核实。于是我们需要一个JWT工具类，用来加密Token和验证Token的有效性。
 */
@Component
@Slf4j
public class JwtUtil {

    //密钥
    @Value("${emos.jwt.secret}")
    private String secret;

    //过期时间（秒）
    @Value("${emos.jwt.expire}")
    private int expire;

    /**
     * 根据用户id获取生成token
     * @param userId
     * @return
     */
    public String createToken(int userId) {
        //设置token生存周期
        Date date = DateUtil.offset(new Date(), DateField.DAY_OF_YEAR, expire).toJdkDate();
        //创建加密算法对象
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTCreator.Builder builder = JWT.create();
        String token = builder.withClaim("userId", userId).withExpiresAt(date).sign(algorithm);
        return token;
    }

    /**
     * 通过toke获取用户id
     * @param token
     * @return
     */
    public Long getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (Exception e) {
            throw new EmosException("令牌无效");
        }
    }

    /**
     * 验证token的有效性，无效会抛出异常
     * @param token
     */
    public void verifierToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret); //创建加密算法对象
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }
}

