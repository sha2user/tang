package com.project.tang.controller;

import com.project.tang.service.SessionListService;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.OkFriendParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("session")
public class SessionController {
    @Autowired
    private SessionListService sessionListService;

    @GetMapping("friList/{id}")
    public Result getFriList(@PathVariable("id")String sid){
        return sessionListService.getFriList(sid);
    }

    @GetMapping("infList/{id}")
    public Result getInfList(@PathVariable("id")String sid){
        return sessionListService.getInfList(sid);
    }
    @GetMapping("reqList/{id}")
    public Result getReqList(@PathVariable("id")String sid){
        return sessionListService.getReqList(sid);
    }
    @PostMapping("okFriend")
    public Result okFriend(@RequestBody OkFriendParam okFriendParam){
        return sessionListService.okFriend(okFriendParam);
    }
    @GetMapping("noFriend/{id}")
    public Result noFriend(@PathVariable("id")String sid){
        return sessionListService.noFriend(sid);
    }
    @GetMapping("addFriend/{id}/{toUserUsername}")
    public Result addFriend(@PathVariable("id")String sid,@PathVariable("toUserUsername")String toUserUsername){
        return sessionListService.addFriend(sid,toUserUsername);
    }

}
