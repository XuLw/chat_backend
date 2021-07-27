package com.example.chat.mapper;

import com.example.chat.entity.ReLa;
import com.example.chat.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    User getById(String id);

    List<ReLa> getFriendsById(String id);
}
