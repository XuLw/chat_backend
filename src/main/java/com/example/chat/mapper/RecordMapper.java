package com.example.chat.mapper;

import com.example.chat.entity.Record;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordMapper {
    boolean insertRecord(Record record);
}
