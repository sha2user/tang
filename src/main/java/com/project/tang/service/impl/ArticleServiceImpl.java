package com.project.tang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.tang.dao.mapper.ArticleBodyMapper;
import com.project.tang.dao.mapper.ArticleCommentMapper;
import com.project.tang.dao.mapper.ArticleMapper;
import com.project.tang.dao.mapper.CategoryMapper;
import com.project.tang.dao.pojo.Article;
import com.project.tang.dao.pojo.ArticleBody;
import com.project.tang.dao.pojo.ArticleComment;
import com.project.tang.dao.pojo.Category;
import com.project.tang.service.ArticleService;
import com.project.tang.vo.ErrorCode;
import com.project.tang.vo.Result;
import com.project.tang.vo.params.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleBodyMapper articleBodyMapper;
    @Autowired
    private ArticleCommentMapper articleCommentMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Result getNewArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .last("limit 6");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result getPriceArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .like("category_name","行情")
                .last("limit 6");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result gettechArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .like("category_name","养殖")
                .last("limit 6");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result getDataArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .like("category_name","资料")
                .last("limit 6");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result getPicArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("create_date")
                .eq("category_name","图片")
                .last("limit 4");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result getHotArticle() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("view_counts")
                .last("limit 7");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result getAllArticleNumber() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.ne("category_name","图片")
                .orderByDesc("create_date");
        int number = articleMapper.selectCount(queryWrapper);
        return Result.success(number);
    }

    @Override
    public Result getCurrent(PageParamSecond pageParamSecond) {
        int currentPage=Integer.parseInt(pageParamSecond.getCurrentPage());
        int pageSize=Integer.parseInt(pageParamSecond.getPageSize());
        if( (currentPage==0) || (pageSize==0)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        Page<Article> page=new Page<>(currentPage,pageSize);
        queryWrapper.orderByDesc("create_date")
                .ne("category_name","图片");
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> list=articlePage.getRecords();
        return Result.success(list);
    }

    @Override
    public Result updateArticle(SecondArticleParam articleParam) {
        Long id = Long.valueOf(articleParam.getId());
        String title= articleParam.getTitle();
        String author =articleParam.getAuthor();
        Integer viewCounts = Integer.valueOf(articleParam.getViewCounts());
        Integer commentCounts =Integer.valueOf(articleParam.getCommentCounts());
        Long time = Long.valueOf(articleParam.getCreateDate());
        String categoryName =articleParam.getCategoryName();
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        Article article = articleMapper.selectById(id);
        article.setTitle(title);
        article.setAuthor(author);
        article.setViewCounts(viewCounts);
        article.setCommentCounts(commentCounts);
        article.setCreateDate(time);
        article.setCategoryName(categoryName);
        articleMapper.updateById(article);
        return Result.success(null);
    }

    @Override
    public Result selectArticle(String keyWord) {
        //管理端关键字查询
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("category_name","图片")
                .like("title",keyWord)
                .or()
                    .like("category_name",keyWord)
                .or()
                    .like("author",keyWord)
                .orderByDesc("create_date");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);

    }



    @Override
    public Result getListByCategory(String name) {
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("category_name",name)
                .orderByDesc("create_date");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }

    @Override
    public Result removeArticle(String sid) {
        if(StringUtils.isBlank(sid)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        Long articleId= Long.valueOf(sid);

        //删除article 和  article_body
        articleMapper.deleteById(articleId);
        //删除article_comment
        QueryWrapper<ArticleComment> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("article_id",articleId);
        //ArticleComment articleComment = articleCommentMapper.selectOne(queryWrapper1);
        if(articleCommentMapper.selectCount(queryWrapper1) == 0){
            //先进行为空判定，为空直接返回，不为空再执行删除操作
            return Result.success(null);
        }
        articleCommentMapper.delete(queryWrapper1);
        return Result.success(null);
    }

    @Override
    public Result addArticle(ArticleParam articleParam) {
        String title=articleParam.getTitle();
        String author = articleParam.getAuthor();
        String categoryName = articleParam.getCategoryName();
        String content = articleParam.getContent();
        if(StringUtils.isAnyBlank(title,author,categoryName,content)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        //添加article
        Article article = new Article();
        article.setAuthor(author);
        article.setCreateDate(System.currentTimeMillis());
        article.setTitle(title);
        article.setCategoryName(categoryName);
        article.setCommentCounts(0);
        article.setViewCounts(0);
        articleMapper.addArticle(article);
        //添加article_body
        ArticleBody articleBody = new ArticleBody();
        articleBody.setArticleId(article.getId());
        articleBody.setContent(content);
        articleBodyMapper.insert(articleBody);


        return Result.success(null);
    }

    @Override
    public Result getPage(PageParam pageParam) {
        int currentPage=Integer.parseInt(pageParam.getCurrentPage());
        int pageSize=Integer.parseInt(pageParam.getPageSize());
        String categoryName= pageParam.getCategoryName();
        if(StringUtils.isBlank(categoryName) || (currentPage==0) || (pageSize==0)){
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        }
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        Page<Article> page=new Page<>(currentPage,pageSize);
        queryWrapper.eq(Article::getCategoryName,categoryName)
                .orderByDesc(Article::getCreateDate);
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        List<Article> list=articlePage.getRecords();
        return Result.success(list);
    }

    @Override
    public Result getHotComList() {
        QueryWrapper<Article> queryWrapper=new QueryWrapper<>();
        queryWrapper.orderByDesc("comment_counts")
                .last("limit 7");
        List<Article> articles = articleMapper.selectList(queryWrapper);
        return Result.success(articles);
    }
}
