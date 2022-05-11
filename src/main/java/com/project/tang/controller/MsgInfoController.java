package com.project.tang.controller;

import com.project.tang.service.MsgInfoService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("message")
public class MsgInfoController {
    @Autowired
    private MsgInfoService msgInfoService;

    @GetMapping("msgList/{id}")
    public Result getMsgList(@PathVariable("id")String sid){
        return msgInfoService.getMsgList(sid);
    }
}
