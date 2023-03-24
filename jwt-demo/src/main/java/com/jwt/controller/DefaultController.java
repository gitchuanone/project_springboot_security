package com.jwt.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * @author yangchuan
 * @date 2023/3/24
 */
@RestController
@RequestMapping("/private/default")
public class DefaultController {
    @Value("${spring.application.name}")
    private String name;

    @GetMapping("/name")
    public Object getApplicationName() {
        return "application name : " + name + "; local time : " + LocalDateTime.now();
    }


}
