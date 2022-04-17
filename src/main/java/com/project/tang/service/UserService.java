package com.project.tang.service;

import com.project.tang.dao.pojo.User;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.LoginParam;
import com.project.tang.vo.params.PasswordParam;

public interface UserService {
    User findUser(String username, String password);
    Result findUserByToken(String token);
    void setToken(LoginParam loginParam, String token);
    User checkToken(String token);
    Result logout(String token);
    User findUserByUsername(String username);
    void saveUser(User user);
    Result changePass(String token,PasswordParam passwordParam);
    Result getUserList();
}
