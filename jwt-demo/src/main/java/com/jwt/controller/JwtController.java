package com.jwt.controller;

import com.jwt.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangchuan
 * @date 2023/3/24
 */
@RestController
@RequestMapping("/public/jwt")
public class JwtController {
    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/token")
    public Object getToken(@RequestParam("userId") String userId,
                           @RequestParam("userName") String userName) {
        return jwtUtils.createToken(userId, userName);
    }

    @GetMapping("/token/parse")
    public Object parseToken(@RequestParam("token") String token) {
        return jwtUtils.parseToken(token).toString();
    }

    @GetMapping("/token/verification")
    public Object verifyToken(@RequestParam("token") String token) {
        return jwtUtils.verifyToken(token).toString();
    }


}
