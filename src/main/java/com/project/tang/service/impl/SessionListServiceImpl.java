package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.FriendRequestMapper;
import com.project.tang.dao.mapper.SessionListMapper;
import com.project.tang.dao.mapper.UserMapper;
import com.project.tang.dao.pojo.ArticleComment;
import com.project.tang.dao.pojo.FriendRequest;
import com.project.tang.dao.pojo.SessionList;
import com.project.tang.dao.pojo.User;
import com.project.tang.service.SessionListService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.OkFriendParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionListServiceImpl implements SessionListService {
    @Autowired
    private SessionListMapper sessionListMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendRequestMapper friendRequestMapper;
    @Override
    public Result getFriList(String sid) {
        QueryWrapper<SessionList> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",Long.valueOf(sid)).orderByDesc("create_date");
        List<SessionList> sessionLists = sessionListMapper.selectList(queryWrapper);
        for (SessionList sessionList:sessionLists){
            String toUserUsername = sessionList.getToUserUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(toUserUsername);
            sessionList.setToUserAvatar(avatarByUsername);
        }
        return Result.success(sessionLists);
    }
    @Override
    public Result getInfList(String sid) {
        List<SessionList> sessionLists = sessionListMapper.findByUserId(Long.valueOf(sid));
        for(SessionList sessionList:sessionLists){
            String toUserUsername = sessionList.getToUserUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(toUserUsername);
            sessionList.setToUserAvatar(avatarByUsername);
        }
        return Result.success(sessionLists);
    }

    @Override
    public Result okFriend(OkFriendParam okFriendParam) {
        Long id =Long.valueOf(okFriendParam.getId());
        Long fromUserId = Long.valueOf(okFriendParam.getFromUserId());
        Long toUserId = Long.valueOf(okFriendParam.getToUserId());
        //删请求
        friendRequestMapper.deleteById(id);
        //创建session list （双向 共两个）
        //1
        String usernameById = userMapper.getUsernameById(toUserId);
        SessionList a = new SessionList();
        a.setUserId(fromUserId);
        a.setToUserId(toUserId);
        a.setUnReadCount(0);
        a.setToUserUsername(usernameById);
        a.setCreateDate(System.currentTimeMillis());
        //2
        String usernameById1 = userMapper.getUsernameById(fromUserId);
        SessionList b = new SessionList();
        b.setUserId(toUserId);
        b.setToUserId(fromUserId);
        b.setUnReadCount(0);
        b.setToUserUsername(usernameById1);
        b.setCreateDate(System.currentTimeMillis());
        //3 insert
        sessionListMapper.insert(a);
        sessionListMapper.insert(b);
        return Result.success(null);
    }

    @Override
    public Result getReqList(String sid) {
        QueryWrapper<FriendRequest> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("to_user_id",Long.valueOf(sid))
                .orderByDesc("create_date");
        List<FriendRequest> friendRequests = friendRequestMapper.selectList(queryWrapper);
        for(FriendRequest friendRequest: friendRequests){
            Long fromUserId = friendRequest.getFromUserId();
            String avatarById = userMapper.getAvatarById(fromUserId);
            String usernameById = userMapper.getUsernameById(fromUserId);
            friendRequest.setFromUserAvatar(avatarById);
            friendRequest.setFromUserUsername(usernameById);
        }
        return Result.success(friendRequests);
    }

    @Override
    public Result noFriend(String sid) {
        friendRequestMapper.deleteById(Long.valueOf(sid));
        return Result.success(null);
    }

    @Override
    public Result addFriend(String sid, String toUserUsername) {
        //先进行用户名是否存在的判定
        QueryWrapper<User> queryWrapper =new QueryWrapper<>();
        queryWrapper.eq("username",toUserUsername);
        if(userMapper.selectCount(queryWrapper)==0){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"用户名不存在");
        }else{
            //再判定是否已经是好友
            Long id = userMapper.selectIdByUsername(toUserUsername);
            Long aLong = sessionListMapper.selectIdByUser(Long.valueOf(sid), id);
            if(aLong == null || aLong.longValue()==0){
                FriendRequest f = new FriendRequest();
                f.setFromUserId(Long.valueOf(sid));
                f.setToUserId(id);
                f.setCreateDate(System.currentTimeMillis());
                friendRequestMapper.insert(f);
                return Result.success(null);
            }else{
                return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),"你们已经是好友了,无需再添加");
            }


        }

    }
}
