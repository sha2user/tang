package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.project.tang.dao.mapper.UserMapper;
import com.project.tang.dao.pojo.User;
import com.project.tang.service.UserService;
import com.project.tang.utils.JWTUtils;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.LoginUserVo;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.LoginParam;
import com.project.tang.vo.params.PasswordParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUser(String username,String password){
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username)
                .eq(User::getPassword,password)
                .select(User::getId,User::getUsername,User::getAdmin,User::getName)
                .last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }


    @Override
    public void setToken(LoginParam loginParam, String token){
        String username= loginParam.getUsername();
        String password=loginParam.getPassword();
        User user=this.findUser(username,password);
        user.setToken(token);
        userMapper.updateById(user);
    }

    @Override
    public User checkToken(String token){
        if(StringUtils.isBlank(token)){
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if(stringObjectMap==null){
            return null;
        }
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getToken,token)
                .select(User::getId,User::getUsername,User::getAdmin,User::getName,User::getAvatar) //??????????????????
                .last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token){
        /**
         * 1 token???????????????
         *      ????????????|??????????????????
         * 2 ?????????????????????????????????
         * 3 ????????????????????????????????????????????????UserVo??????
         */
        //1

        User user=this.checkToken(token);
        //2
        if(user==null){
            return Result.fail(ErrorCode.TOKEN_ERROR.getCode(), ErrorCode.TOKEN_ERROR.getMsg());
        }
        //3
        LoginUserVo loginUserVo=new LoginUserVo();
        loginUserVo.setId(String.valueOf(user.getId()));
        loginUserVo.setUsername(user.getUsername());
        loginUserVo.setAdmin(String.valueOf(user.getAdmin()));
        loginUserVo.setName(user.getName());
        loginUserVo.setAvatar(user.getAvatar());

        return Result.success(loginUserVo);
    }


    @Override
    public Result logout(String token){
       User user=this.checkToken(token);
        //???????????????
        UpdateWrapper<User> updateWrapper=new UpdateWrapper<>();
        updateWrapper.eq("id",user.getId())
                .set("token",null);
        /*
          ?????????????????????  .eq("id",user.getId()) ??????????????????
          ???updatewWapper.set????????????me_user????????????????????? token=null ?????????????????????????????????????????????????????????????????????user?????????
          ????????????????????????id??????where?????????
          ???mysql?????????????????????
        */

        //??????userMapper??????update
        userMapper.update(user,updateWrapper);
        return Result.success(null);
    }


    @Override
    public User findUserByUsername(String username){
        LambdaQueryWrapper<User> queryWrapper= new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username)
                .select(User::getId,User::getUsername,User::getAdmin,User::getName)
                .last("limit 1");
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public void saveUser(User user){
        if(user.getToken()==null){
            userMapper.insert(user);
        }else{
            userMapper.updateById(user);
        }

    }

    @Override
    public Result changePass(String token, PasswordParam passwordParam) {
        //String username=passwordParam.getUsername();
        String oldPass= passwordParam.getOldPass();
        String checkPass=passwordParam.getCheckPass();
        if( StringUtils.isBlank(token) || StringUtils.isBlank(oldPass) || StringUtils.isBlank(checkPass)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        User user=this.checkToken(token); //???user???user1????????????user????????????user?????????password??????
        Long id = user.getId();
        User user1 = userMapper.selectById(id);
        String password = user1.getPassword();

        if(oldPass.equals(password) ){
            user.setPassword(checkPass);
            userMapper.updateById(user);
            return Result.success(null);

        }else{
            //?????????????????????????????????????????????
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

    }

    @Override
    public Result getUserList() {
        //???????????????admin=0???????????????
        int admin=0;
        QueryWrapper<User> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("admin",admin);
        List<User> users = userMapper.selectList(queryWrapper);
        return Result.success(users);
    }

    @Override
    public void setAvatarById(String sid, String fileUrl) {
        Long id=Long.valueOf(sid);
        User user = userMapper.selectById(id);
        user.setAvatar(fileUrl);
        userMapper.updateById(user);
    }

}
