package com.example.chat.service;

import com.example.chat.entity.Record;
import com.example.chat.mapper.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RecordService {

    @Autowired
    RecordMapper mapper;

    public boolean insertRecord(Record record) {
        return mapper.insertRecord(record);
    }

}
