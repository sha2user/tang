package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.tang.dao.mapper.*;
import com.project.tang.dao.pojo.*;
import com.project.tang.service.MomentService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.MomentParam;
import com.project.tang.vo.params.MomentUpdate;
import com.project.tang.vo.params.PageParamSecond;
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
    @Autowired
    private GetLikeMapper getLikeMapper;
    @Autowired
    private UserMapper userMapper;

    @Override
    public Result getMomentList() {
        QueryWrapper<Moment> queryWrapper =new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        //将每个moment中根据username，给avatar赋值
        for (Moment moment:moments){
            String username = moment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username);
            moment.setAvatar(avatarByUsername);
        }
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
        for (MomentComment momentComment:momentComments){
            String username = momentComment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username);
            momentComment.setAvatar(avatarByUsername);
        }
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
        for (Moment moment:moments){
            String username = moment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username);
            moment.setAvatar(avatarByUsername);
        }
        return Result.success(moments);
    }

    @Override
    public Result getLikeMoList() {
        QueryWrapper<Moment> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("nice")
                .last("limit 5");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        for (Moment moment:moments){
            String username = moment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username);
            moment.setAvatar(avatarByUsername);
        }
        return Result.success(moments);
    }

    @Override
    public Result addMoment(MomentParam momentParam) {
        String title=momentParam.getTitle();
        String sid = momentParam.getId();
        String username = momentParam.getUsername();
        String content = momentParam.getContent();
        if(StringUtils.isAnyBlank(title,sid,username,content)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //添加moment
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

    @Override
    public Result getCurrent(PageParamSecond pageParamSecond) {
        int currentPage=Integer.parseInt(pageParamSecond.getCurrentPage());
        int pageSize=Integer.parseInt(pageParamSecond.getPageSize());
        if( (currentPage==0) || (pageSize==0)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        QueryWrapper<Moment> queryWrapper=new QueryWrapper<>();
        Page<Moment> page=new Page<>(currentPage,pageSize);
        queryWrapper.orderByDesc("create_date");
        Page<Moment> momentPage = momentMapper.selectPage(page, queryWrapper);
        List<Moment> list=momentPage.getRecords();
        return Result.success(list);
    }

    @Override
    public Result updateMoment(MomentUpdate momentUpdate) {
        Long id = Long.valueOf(momentUpdate.getId());
        String title= momentUpdate.getTitle();
        String content = momentUpdate.getContent();
        Long createDate = Long.valueOf(momentUpdate.getCreateDate());
        QueryWrapper<Moment> queryWrapper = new QueryWrapper<>();
        Moment moment = momentMapper.selectById(id);
        moment.setTitle(title);
        moment.setContent(content);
        moment.setCreateDate(createDate);
        momentMapper.updateById(moment);
        return Result.success(null);
    }

    @Override
    public Result selectMoment(String keyWord) {
        if(StringUtils.isBlank(keyWord)){
            Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        QueryWrapper<Moment> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title",keyWord)
                .or()
                    .like("username",keyWord)
                .orderByDesc("create_date");
        List<Moment> moments = momentMapper.selectList(queryWrapper);
        for (Moment moment:moments){
            String username = moment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username);
            moment.setAvatar(avatarByUsername);
        }
        return Result.success(moments);
    }

    @Override
    public Result selectMyMoment(String username) {
        if(StringUtils.isBlank(username)){
            Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        QueryWrapper<Moment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username)
                .orderByDesc("create_date");
        if(momentMapper.selectCount(queryWrapper) != 0){
            List<Moment> moments = momentMapper.selectList(queryWrapper);
            for (Moment moment:moments){
                String avatarByUsername = userMapper.getAvatarByUsername(username);
                moment.setAvatar(avatarByUsername);
            }
            return Result.success(moments);
        }else{
            //若为空，返回false，前端进行逻辑判定
            return  Result.success(false);
        }

    }

    @Override
    public Result selectMyComment(String username) {
        List<Moment> moments = getLikeMapper.selectMyComment(username);
        for (Moment moment:moments){
            String username1 = moment.getUsername();
            String avatarByUsername = userMapper.getAvatarByUsername(username1);
            moment.setAvatar(avatarByUsername);
        }
        return Result.success(moments);

    }

    @Override
    public Result selectMyLike(String userId) {
        //此处操作为多表查询，需要同时查询moment 和 like 表
        //直接创建一对儿Mapper映射( GetLikeMapper接口 以及 GetLikeMapper.xml )，在其中键入mysql语句直接实现功能。
        List<Moment> myLike = getLikeMapper.getMyLike(Long.valueOf(userId));
        if(myLike.isEmpty()){
            return Result.success(false);
        }else {
            for (Moment moment:myLike){
                String username = moment.getUsername();
                String avatarByUsername = userMapper.getAvatarByUsername(username);
                moment.setAvatar(avatarByUsername);
            }
            return Result.success(myLike);
        }
    }
}
