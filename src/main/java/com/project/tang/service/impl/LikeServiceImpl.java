package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.LikeMapper;
import com.project.tang.dao.mapper.MomentMapper;
import com.project.tang.dao.pojo.Like;
import com.project.tang.service.LikeService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.LikeParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikeMapper likeMapper;
    @Autowired
    private MomentMapper momentMapper;


    @Override
    public Boolean getLikeStatus(Long momentId, Long userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("moment_id",momentId)
                .eq("user_id",userId);
        if(likeMapper.selectCount(queryWrapper) == 1){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void setLike(Long momentId, Long userId) {
        Like like = new Like();
        like.setMomentId(momentId);
        like.setUserId(userId);
        likeMapper.insert(like);
    }
    @Override
    public void deleteLike(Long momentId, Long userId) {
        QueryWrapper<Like> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("moment_id",momentId).
                eq("user_id",userId);
        likeMapper.delete(queryWrapper);

    }

    @Override
    public Result addNice(LikeParam likeParam) {
        String sid = likeParam.getMomentId();
        String sId = likeParam.getUserId();
        if(StringUtils.isAnyBlank(sid,sId)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long momentId= Long.valueOf(sid);
        Long userId = Long.valueOf(sId);
        Boolean likeStatus = this.getLikeStatus(momentId, userId);
        if(likeStatus){
            return Result.success(likeStatus);
        }else{
            this.setLike(momentId,userId);
            //moment 表中nice +1
            momentMapper.addNice(momentId);
            return Result.success(true);
        }

    }

    @Override
    public Result reduceNice(LikeParam likeParam) {
        String sid = likeParam.getMomentId();
        String sId = likeParam.getUserId();
        if(StringUtils.isAnyBlank(sid,sId)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long momentId= Long.valueOf(sid);
        Long userId = Long.valueOf(sId);
        Boolean likeStatus = this.getLikeStatus(momentId, userId);
        if(likeStatus){
            this.deleteLike(momentId,userId);
            //moment 表中nice -1
            momentMapper.reduceNice(momentId);
            return Result.success(false);
        }else{
            return Result.success(false);
        }
    }




}
