package com.example.chat.service;

import com.example.chat.entity.ReLa;
import com.example.chat.entity.User;
import com.example.chat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserMapper mapper;

    public User getById(String id) {
        return mapper.getById(id);
    }

    public List<ReLa> getFriendsById(String id) {
        return mapper.getFriendsById(id);
    }
}
