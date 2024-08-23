package com.manqin.service2.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class IndexController {

    @Value("${spring.application.name}")
    private String serviceName;
    @Autowired
    private Environment env;
    public static final String[] NAMES = new String[]{"Paul", "John", "Ringo", "George"};
    private static final Random r = new Random();

    @GetMapping
    public String getName(HttpServletResponse response) {
        response.addHeader("k8s-host", env.getProperty("HOSTNAME"));
        String msg ="当前服务："+ serviceName+" ";
        return msg + NAMES[r.ints(0, NAMES.length).limit(1).findFirst().getAsInt()];
    }
}
