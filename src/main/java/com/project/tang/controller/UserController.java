package com.project.tang.controller;

import com.project.tang.service.UserService;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.PasswordParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("currentUser")
    public Result currentUser(@RequestHeader("Authorization")String token){
        return userService.findUserByToken(token);
    }
    @PostMapping("changePass")
    public Result changePass(@RequestHeader("Authorization")String token,@RequestBody PasswordParam passwordParam){
        return userService.changePass(token,passwordParam);
    }
    @PostMapping("getUserList")
    public Result getUserList(){
        return userService.getUserList();
    }
}
