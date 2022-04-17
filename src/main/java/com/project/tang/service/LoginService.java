package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.LoginParam;
import com.project.tang.vo.params.RegisterParam;

public interface LoginService {
    Result login(LoginParam loginParam);
    Result register(RegisterParam registerParam);
}
