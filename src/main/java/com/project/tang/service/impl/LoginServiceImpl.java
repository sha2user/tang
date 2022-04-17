package com.project.tang.service.impl;

import com.project.tang.dao.pojo.User;
import com.project.tang.service.LoginService;
import com.project.tang.service.UserService;
import com.project.tang.utils.JWTUtils;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.LoginParam;
import com.project.tang.vo.params.RegisterParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private UserService userService;
    //private static final String salt = "Tang"; //加密盐


    @Override
    public Result login(LoginParam loginParam){
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询是否存在
         * 3. 若不存在 登录失败
         * 4. 若存在 使用jwt生成token返回给前端
         * 5. token放入数据库user表中
         * 6.（有需要登录认证的时候，先认证token字符串是否合法，再去数据库认证是否存在）
         */
        String username= loginParam.getUsername();
        String password=loginParam.getPassword();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        User user = userService.findUser(username,password);//返回的user有 id username admin name
        if(user==null){
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token =JWTUtils.createToken(user.getId()); //创建token
        //存储token
                    //HttpSession session = request.getSession();
                    //session.setAttribute(token,JSON.toJSONString(user));
        //user.setToken(token); //此user用来返回token、name
        userService.setToken(loginParam,token);
        //将token返回给前端
        return Result.success(token);
    }


    @Override
    public Result register(RegisterParam registerParam){
        /**
         * 1 判断参数是否合法
         * 2 判断账户是否存在，若存在，返回账户已经被注册
         * 3 如果账户不存在，注册用户
         * 4 生成token，存入redis并返回token
         * 5 注意：加上事务。一旦中间任何过程出现问题，注册的用户需要回滚
         *  （测试：关掉redis后进行注册，数据库中添加了新的用户信息。理论上我们是不允许这种情况出现的。所以要加事务）
         */
        String username = registerParam.getUsername();
        String password = registerParam.getPassword();
        String name = registerParam.getName();
        String phonenumber = registerParam.getPhonenumber();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)||StringUtils.isBlank(name)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        User user=userService.findUserByUsername(username);
        if(user!=null){
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        User user1=new User();
        //user1用来充当userMapper的user参数。
        user1.setUsername(username);
        user1.setPassword(password);
        user1.setName(name);
        user1.setPhoneNumber(phonenumber);
        user1.setAdmin(0);
        user1.setCreateDate(System.currentTimeMillis());
        userService.saveUser(user1);
        //user2用来获取user1的id，并settoken，其实两者同一。
        User user2=userService.findUserByUsername(username);
        String token = JWTUtils.createToken(user2.getId());
        //存入token
        user2.setToken(token);
        userService.saveUser(user2);
        return Result.success(token);

    }
}
