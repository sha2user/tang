package com.project.tang.service.impl;

import com.project.tang.dao.mapper.MomentCommentMapper;
import com.project.tang.dao.mapper.MomentMapper;
import com.project.tang.dao.pojo.ArticleComment;
import com.project.tang.dao.pojo.MomentComment;
import com.project.tang.service.MomentCommentService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.MomentCommentParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MomentCommentServiceImpl implements MomentCommentService {
    @Autowired
    private MomentCommentMapper momentCommentMapper;
    @Autowired
    private MomentMapper momentMapper;

    @Override
    public Result submitComment(MomentCommentParam momentCommentParam) {
        String sid= momentCommentParam.getMomentId();
        String content = momentCommentParam.getContent();
        String username = momentCommentParam.getUsername();
        if(StringUtils.isAnyBlank(sid,content,username)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        MomentComment momentComment = new MomentComment();
        momentComment.setMomentId(Long.valueOf(sid));
        momentComment.setContent(content);
        momentComment.setCreateDate(System.currentTimeMillis());
        momentComment.setUsername(username);
        momentCommentMapper.insert(momentComment);
        //评论成功以后，moment中的评论数要+1
        momentMapper.addCommentCounts(Long.valueOf(sid));
        return Result.success(null);
    }
}
