package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.LikeMapper;
import com.project.tang.dao.mapper.MomentCommentMapper;
import com.project.tang.dao.mapper.MomentMapper;
import com.project.tang.dao.pojo.*;
import com.project.tang.service.MomentService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.MomentParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MomentServiceImpl implements MomentService {
    @Autowired
    private MomentCommentMapper momentCommentMapper;
    @Autowired
    private MomentMapper momentMapper;
    @Autowired
    private LikeMapper likeMapper;

    @Override
    public Result getMomentList() {
        QueryWrapper<Moment> queryWrapper =new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        return Result.success(moments);
    }

    @Override
    public Result getMoComment(String sid) {
        if(StringUtils.isBlank(sid)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long id = Long.valueOf(sid);
        QueryWrapper<MomentComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("moment_id",id)
                .orderByDesc("create_date");
        List<MomentComment> momentComments = momentCommentMapper.selectList(queryWrapper);
        return Result.success(momentComments);
    }

    @Override
    public Result removeCommentById(String sid) {
        Long id = Long.valueOf(sid);
        MomentComment momentComment = momentCommentMapper.selectById(id);
        Long momentId = momentComment.getMomentId();
        momentCommentMapper.deleteById(id);
        momentMapper.subCommentCounts(momentId); //帖子的评论数减1
        return Result.success(null);
    }

    @Override
    public Result getHotComList() {
        QueryWrapper<Moment> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("comment_number")
                .last("limit 5");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        return Result.success(moments);
    }

    @Override
    public Result getLikeMoList() {
        QueryWrapper<Moment> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("nice")
                .last("limit 5");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        return Result.success(moments);
    }

    @Override
    public Result addArticle(MomentParam momentParam) {
        String title=momentParam.getTitle();
        String sid = momentParam.getId();
        String username = momentParam.getUsername();
        String content = momentParam.getContent();
        if(StringUtils.isAnyBlank(title,sid,username,content)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //添加article
        Moment moment = new Moment();
        moment.setUserId(Long.valueOf(sid));
        moment.setCreateDate(System.currentTimeMillis());
        moment.setTitle(title);
        moment.setContent(content);
        moment.setUsername(username);
        momentMapper.insert(moment);
        return Result.success(null);
    }

    @Override
    public Result removeMoment(String sid) {
        if(StringUtils.isBlank(sid)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long id= Long.valueOf(sid);
        //删除moment
        momentMapper.deleteById(id);
        //删除moment_comment
        QueryWrapper<MomentComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("moment_id",id);
        if(momentCommentMapper.selectCount(queryWrapper) == 0){
            //删除 like表中对应的数据
            QueryWrapper<Like> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("moment_id",id); //某一个moment被删除，其对应的所有点赞记录皆不存在，故使用一个eq 条件就可以做到
            likeMapper.delete(queryWrapper1);
            return Result.success(null);
        }
        momentCommentMapper.delete(queryWrapper);

        return Result.success(null);
    }
}
