package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.MsgInfoMapper;
import com.project.tang.dao.mapper.SessionListMapper;
import com.project.tang.dao.mapper.UserMapper;
import com.project.tang.dao.pojo.MsgInfo;
import com.project.tang.dao.pojo.SessionList;
import com.project.tang.dao.pojo.User;
import com.project.tang.service.MsgInfoService;
import com.project.tang.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MsgInfoServiceImpl implements MsgInfoService {
    @Autowired
    private MsgInfoMapper msgInfoMapper;
    @Autowired
    private SessionListMapper sessionListMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getMsgList(String sid) {
        SessionList sessionList = sessionListMapper.selectById(Long.valueOf(sid));
        if(sessionList == null){
            return Result.success(null);
        }
        Long userId = sessionList.getUserId();
        Long toUserId = sessionList.getToUserId();
        //查询用户头像
        String userAvatar = userMapper.getAvatarById(userId);
        String toUserAvatar = userMapper.getAvatarById(toUserId);
        //查询消息列表
        /*QueryWrapper<MsgInfo> queryWrapper= new QueryWrapper<>();
        queryWrapper.and(wrapper ->wrapper.and(i->i.eq("from_user_id",userId).eq("to_user_id",toUserId))
                .or()
                .and(i->i.eq("from_user_id",toUserId).eq("to_user_id",userId))
                        );*/
        List<MsgInfo> msgInfos = msgInfoMapper.selectMessageList(userId,toUserId);
        for(MsgInfo msgInfo:msgInfos){
            msgInfo.setFromUserAvatar(userAvatar);
            msgInfo.setToUserAvatar(toUserAvatar);
        }
        return Result.success(msgInfos);
    }
}
