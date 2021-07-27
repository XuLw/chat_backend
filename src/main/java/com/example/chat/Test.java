package com.example.chat;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.ReLa;
import com.example.chat.entity.User;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        List<ReLa> userList = new ArrayList<>();
        userList.add(new ReLa());
        userList.add(new ReLa());
        String json = JSON.toJSONString(userList);
        System.out.println(json);
        List<ReLa> list = JSON.parseArray(json, ReLa.class);
        System.out.println(list.size());
    }
}
