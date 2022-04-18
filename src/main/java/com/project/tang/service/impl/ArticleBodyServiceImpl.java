package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.tang.dao.mapper.ArticleBodyMapper;
import com.project.tang.dao.mapper.ArticleMapper;
import com.project.tang.dao.pojo.Article;
import com.project.tang.dao.pojo.ArticleBody;
import com.project.tang.service.ArticleBodyService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.ContentParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleBodyServiceImpl implements ArticleBodyService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;

    @Override
    public Result getArticleContentById(String sid) {
        if(StringUtils.isBlank(sid)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long id= Long.valueOf(sid);
        //查询文章详情
        QueryWrapper<ArticleBody> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",id);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        //查询文章详情以后 viewCounts要+1.
        Article article = articleMapper.selectById(id);
        Integer oldViewCounts = article.getViewCounts();
        Integer newViewCounts = oldViewCounts+1;
        article.setViewCounts(newViewCounts);
        articleMapper.updateById(article);
        return Result.success(articleBody);
    }

    @Override
    public Result submitContent(ContentParam contentParam) {
        String content = contentParam.getContent();
        String sid = contentParam.getArticleId();
        if(StringUtils.isAnyBlank(sid,content)){
            return  Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long articleId =Long.valueOf(sid);

        QueryWrapper<ArticleBody> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id",articleId);
        ArticleBody articleBody = articleBodyMapper.selectOne(queryWrapper);
        if(articleBody.getContent() == content){
            return Result.success(null);
        }else{
            articleBody.setContent(content);
            articleBodyMapper.updateById(articleBody);
            return Result.success(null);
        }

    }

}
