package com.project.tang.service;

import com.project.tang.vo.Result;
import com.project.tang.vo.params.ArticleParam;
import com.project.tang.vo.params.PageParam;

public interface ArticleService {
    Result getNewArticle();
    Result getPriceArticle();
    Result gettechArticle();
    Result getDataArticle();
    Result getPicArticle();
    Result getHotArticle();
    Result getListByCategory(String name);
    Result removeArticle(String sid);
    Result addArticle(ArticleParam articleParam);
    Result getPage(PageParam pageParam);
    Result getHotComList();
}
