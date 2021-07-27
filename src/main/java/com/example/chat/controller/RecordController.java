package com.example.chat.controller;

import com.alibaba.fastjson.JSON;
import com.example.chat.entity.Record;
import com.example.chat.service.RecordService;
import com.example.chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/record")
public class RecordController {

    private static final Logger log = LoggerFactory.getLogger(RecordController.class);

    @Autowired
    RecordService service;

    @RequestMapping("/insertTest")
    public String getUser() {
        Record record = new Record();
        record.setId1("3123");
        record.setId2("312fasdf3");
        record.setSend_date(new Date());
        record.setText("fasdfdsa");
        boolean bo = service.insertRecord(record);
        return "" + bo;
    }

}
