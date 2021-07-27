package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @RequestMapping("/getUser/{id}")
    public String getUser(@PathVariable String id) {
        log.info(id);
        return userService.getById(id).toString();
    }

    @RequestMapping("/getRela/{id}")
    public String getFriends(@PathVariable String id) {
        log.info("获取好友列表：" + id);
        String s = JSON.toJSONString(userService.getFriendsById(id));
        log.info(s);
        return s;
    }

    @RequestMapping("/test")
    public String getUser() {
        log.info("ffff");
        return "hello world";
    }

}
