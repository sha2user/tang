package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.ArticleCommentMapper;
import com.project.tang.dao.mapper.ArticleMapper;
import com.project.tang.dao.pojo.Article;
import com.project.tang.dao.pojo.ArticleComment;
import com.project.tang.service.ArticleCommentService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.ArticleCommentParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCommentServiceImpl implements ArticleCommentService {
    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Override
    public Result submitComment(ArticleCommentParam articleCommentParam) {
        String sid= articleCommentParam.getArticleId();
        String content = articleCommentParam.getContent();
        String username = articleCommentParam.getUsername();
        if(StringUtils.isAnyBlank(sid,content,username)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        ArticleComment articleComment = new ArticleComment();
        articleComment.setArticleId(Long.valueOf(sid));
        articleComment.setContent(content);
        articleComment.setCreateDate(System.currentTimeMillis());
        articleComment.setUsername(username);
        articleCommentMapper.insert(articleComment);
        //评论成功以后，article中的评论数要+1
        articleMapper.addCommentCounts(Long.valueOf(sid));
        return Result.success(null);
    }

    @Override
    public Result getArticleCommentById(String sid) {
        Long articleId = Long.valueOf(sid);
        QueryWrapper<ArticleComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .eq("article_id",articleId);
        List<ArticleComment> articleComments = articleCommentMapper.selectList(queryWrapper);
        return Result.success(articleComments);
    }

    @Override
    public Result removeCommentById(String sid) {
        Long id = Long.valueOf(sid);
        ArticleComment articleComment = articleCommentMapper.selectById(id);
        Long articleId = articleComment.getArticleId();
        articleCommentMapper.deleteById(id);
        articleMapper.subCommentCounts(articleId); //文章的评论数减1
        return Result.success(null);
    }
}
