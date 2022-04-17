package com.project.tang.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.tang.dao.pojo.Article;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleMapper extends BaseMapper<Article> {
    void addCommentCounts(Long id);
    void subCommentCounts(Long id);
    void addArticle(Article article);
}
