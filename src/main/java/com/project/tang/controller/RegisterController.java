package com.project.tang.controller;

import com.project.tang.service.LoginService;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.RegisterParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {
    @Autowired
    private LoginService loginService;
    @PostMapping
    public Result Register(@RequestBody RegisterParam registerParam){

        return loginService.register(registerParam);
    }
}
