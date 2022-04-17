package com.project.tang.controller;


import com.project.tang.service.UserService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("logout")
public class LogoutController {
    @Autowired
    private UserService userService;
    @GetMapping
    public Result logout(@RequestHeader("Authorization") String token){
        return userService.logout(token);
    }
}
