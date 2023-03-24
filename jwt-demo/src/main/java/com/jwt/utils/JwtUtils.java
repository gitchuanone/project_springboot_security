package com.jwt.utils;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangchuan
 * @date 2023/3/24
 */
@Component
public class JwtUtils {
    @Value("${define.jwt.secretKey:RandomSecret}")
    private String secretKey;

    @Value("${define.jwt.tokenExpireSecond:5}")
    private int tokenExpireTime;

    public static String secretKeyValue;

    private static int tokenExpireValue;

    /**
     * 注解说明
     * Constructor(构造方法) -> @Autowired(依赖注入) -> @PostConstruct(注释的方法)
     */
    @PostConstruct
    public void init() {
        secretKeyValue = secretKey;
        tokenExpireValue = tokenExpireTime;
    }

    /**
     * 生成Token
     * @param userId
     * @param userName
     * @return
     * @throws Exception
     */
    public static String createToken(String userId, String userName) {
        try {
            System.out.println("生成Token，秘钥；" + secretKeyValue + ";过期时间：" + tokenExpireValue);
            Calendar nowTime = Calendar.getInstance();
            nowTime.add(Calendar.MINUTE, tokenExpireValue);
            Date expireDate = nowTime.getTime();
            Map<String, Object> map = new HashMap<>();
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            String token = JWT.create()
                    .withHeader(map)//头
                    .withClaim("userId", userId)
                    .withClaim("userName", userName)
                    .withSubject("测试")//
                    .withIssuedAt(new Date())//签名时间
                    .withExpiresAt(expireDate)//过期时间
                    .sign(Algorithm.HMAC256(secretKeyValue));//签名
            return token;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 验证Token
     * @param token
     * @return
     * @throws Exception
     */
    public static Map<String, Claim> verifyToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKeyValue)).build();
            DecodedJWT jwt = null;
            try {
                jwt = verifier.verify(token);
            }catch (Exception e){
                throw new RuntimeException("凭证已过期，请重新登录");
            }
            return jwt.getClaims();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析Token
     * @param token
     * @return
     */
    public Map<String, Claim> parseToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }

}
